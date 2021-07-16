package com.deltaqin.scussm.mq;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.deltaqin.scussm.common.CommunityConstant;
import com.deltaqin.scussm.common.utils.JSONStringUtil;
import com.deltaqin.scussm.common.utils.RedisKeyUtil;
import com.deltaqin.scussm.entity.DiscussPost;
import com.deltaqin.scussm.entity.Message;
import com.deltaqin.scussm.entity.User;
import com.deltaqin.scussm.netty.MyWebSocketHandler;
import com.deltaqin.scussm.service.DiscussPostService;
import com.deltaqin.scussm.service.ElasticsearchService;
import com.deltaqin.scussm.service.MessageService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import com.qiniu.util.StringMap;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.extern.slf4j.Slf4j;
//import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.core.ZSetOperations;
//import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

import static com.deltaqin.scussm.common.utils.JSONStringUtil.getJSONString;

/**
 * @author deltaqin
 * @date 2021/6/24 上午12:25
 */

@Component
@Slf4j
public class MqConsumer implements CommunityConstant {

    private static ObjectMapper MAPPER = new ObjectMapper();

    private DefaultMQPushConsumer consumer;

    @Value("${mq.nameserver.addr}")
    private String nameServerAddr;

    @Value("${mq.topicname}")
    private String topicName;




    @Autowired
    private MessageService messageService;

    @Autowired
    private DiscussPostService discussPostService;

    @Autowired
    private ElasticsearchService elasticsearchService;

    @Value("${wk.image.command}")
    private String wkImageCommand;

    @Value("${wk.image.storage}")
    private String wkImageStorage;

    @Value("${qiniu.key.access}")
    private String accessKey;

    @Value("${qiniu.key.secret}")
    private String secretKey;

    @Value("${qiniu.bucket.share.name}")
    private String shareBucketName;

    @Autowired
    private ThreadPoolTaskScheduler taskScheduler;

    @Autowired
    private RedisTemplate redisTemplate;


    @PostConstruct
    public void init() throws MQClientException {
        consumer = new DefaultMQPushConsumer(CONSUMER_GROUP);
        consumer.setNamesrvAddr(nameServerAddr);
        consumer.subscribe(topicName, "*");
        consumer.registerMessageListener(new MessageListenerConcurrently() {
            // 注册监听器
            @Override
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> list, ConsumeConcurrentlyContext consumeConcurrentlyContext) {
                org.apache.rocketmq.common.message.Message message = list.get(0);
                String jsonString = new String(message.getBody());
                Event event = JSON.parseObject(jsonString, Event.class);
                String topic = event.getTopic();

                switch (topic){
                    case TOPIC_COMMENT: case TOPIC_LIKE: case TOPIC_FOLLOW:
                        handleCommentMessage(event);
                        break;
                    case TOPIC_CHAT:
                        handleChatMessage(event);
                        break;
                    case TOPIC_PUBLISH:
                        handlePublishMessage(event);
                        break;
                    case TOPIC_DELETE:
                        handleDeleteMessage(event);
                        break;
                    case TOPIC_SHARE:
                        handleShareMessage(event);
                        break;
                }

                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }
        });

        consumer.start();
    }


    // 评论点赞关注（持久化到数据库）
    // DONE 加上使用websocket实时通知，数据库依旧是要持久化的，不然下次没了
    // 所以在写到数据库或者Redis的同时，将消息发给前端
    //@KafkaListener(topics = {TOPIC_COMMENT, TOPIC_LIKE, TOPIC_FOLLOW})
    public void handleCommentMessage(Event event) {
        if (event == null) {
            log.error("消息的内容为空!");
            return;
        }

        // DONE 修复自己赞自己提醒
        //只有不是自己赞/评论自己才会触发Message
        if (event.getEntityUserId() != event.getUserId()) {
            // 发送站内通知
            Message message = new Message();
            message.setFromId(SYSTEM_USER_ID);
            message.setToId(event.getEntityUserId());
            message.setConversationId(event.getTopic());
            message.setCreateTime(new Date());

            Map<String, Object> content = new HashMap<>();
            content.put("userId", event.getUserId());
            content.put("entityType", event.getEntityType());
            content.put("entityId", event.getEntityId());

            if (!event.getData().isEmpty()) {
                for (Map.Entry<String, Object> entry : event.getData().entrySet()) {
                    content.put(entry.getKey(), entry.getValue());
                }
            }

            message.setContent(JSONObject.toJSONString(content));
            messageService.addMessage(message);


            //// 获取当前用户是否在线
            //Channel channel = MyWebSocketHandler.userChannelMap.get(event.getEntityId());
            ////如果连接不为空，表示用户在线
            ////封装返回数据
            //if (channel != null) {
            //    Map<String, Object> map = new HashMap<>();
            //    map.put("content", event.getData().get("content"));
            //    map.put("target", event.getData().get("targetUser"));
            //    // 把数据通过WebSocket连接主动推送用户
            //    channel.writeAndFlush(new TextWebSocketFrame(getJSONString(0, "ok", map)));
            //}

        }
        log.info("消费成功");
    }

    //@KafkaListener(topics = {TOPIC_CHAT})
    public void handleChatMessage(Event event) {
        if (event == null) {
            log.error("消息的内容为空!");
            return;
        }

        // 获取目标用户是否在线
        Channel channel = MyWebSocketHandler.userChannelMap.get(event.getEntityId());
        //如果连接不为空，表示用户在线
        //封装返回数据
        if (channel != null) {
            Map<String, Object> map = new HashMap<>();
            map.put("content", event.getData().get("content"));
            JSONObject targetUser = (JSONObject) event.getData().get("targetUser");

            map.put("headerUrl", targetUser.getString("headerUrl"));
            map.put("id", targetUser.getIntValue("id"));
            map.put("username", targetUser.getString("username"));
            // 把数据通过WebSocket连接主动推送用户
            channel.writeAndFlush(new TextWebSocketFrame(getJSONString(0, "ok", map)));
        }
    }

    // 消费发帖事件到ES
    //@KafkaListener(topics = {TOPIC_PUBLISH})
    public void handlePublishMessage(Event event) {
        if (event == null) {
            log.error("消息的内容为空!");
            return;
        }

        // 1. 查询帖子的信息放到ES里面
        DiscussPost post = discussPostService.findDiscussPostById(event.getEntityId());
        elasticsearchService.saveDiscussPost(post);

        // 2. 查询帖子作者的关注者，通知这些人有新文章
        // 			// 3 就是用户实体，1和2一个是评论一个是回复
        String followerKey = RedisKeyUtil.getFollowerKey(3, event.getUserId());
        //Cursor<> scan = redisTemplate.opsForZSet().scan(followerKey, ScanOptions.NONE);
        Cursor<ZSetOperations.TypedTuple<Object>> cursor =
                redisTemplate.opsForZSet().scan(followerKey, ScanOptions.NONE);
        while (cursor.hasNext()) {
            ZSetOperations.TypedTuple<Object> typedTuple = cursor.next();
            //System.out.println("通过scan(K key, ScanOptions options)方法获取匹配元素:" +
            //         + "--->" + typedTuple.getScore());
            Integer value = (Integer)typedTuple.getValue();

            // 发送站内通知
            Message message = new Message();
            // 系统的发送者就是 1
            message.setFromId(SYSTEM_USER_ID);
            message.setToId(value);
            // 这个属性不同消息的内容是不一样的，有每一个消费者都对应不同的消息类型，还有私信的消息也在这
            message.setConversationId(event.getTopic());
            message.setCreateTime(new Date());

            Map<String, Object> content = new HashMap<>();
            content.put("userId", event.getUserId());
            // 帖子是1
            content.put("entityType", event.getEntityType());
            // ID 是帖子的
            content.put("entityId", event.getEntityId());

            if (!event.getData().isEmpty()) {
                for (Map.Entry<String, Object> entry : event.getData().entrySet()) {
                    content.put(entry.getKey(), entry.getValue());
                }
            }
            message.setContent(JSONObject.toJSONString(content));
            messageService.addMessage(message);
        }
    }

    // 消费删帖事件到ES
    //@KafkaListener(topics = {TOPIC_DELETE})
    public void handleDeleteMessage(Event event) {
        if (event == null) {
            log.error("消息格式错误!");
            return;
        }

        elasticsearchService.deleteDiscussPost(event.getEntityId());
    }

    // 消费分享事件上传到七牛云
    //@KafkaListener(topics = TOPIC_SHARE)
    public void handleShareMessage(Event event) {
        if (event == null) {
            log.error("消息格式错误!");
            return;
        }

        String htmlUrl = (String) event.getData().get("htmlUrl");
        String fileName = (String) event.getData().get("fileName");
        String suffix = (String) event.getData().get("suffix");

        String cmd = wkImageCommand + " --quality 75 "
                + htmlUrl + " " + wkImageStorage + "/" + fileName + suffix;
        try {
            Runtime.getRuntime().exec(cmd);
            log.info("生成长图成功: " + cmd);
        } catch (IOException e) {
            log.error("生成长图失败: " + e.getMessage());
        }

        // 启用定时器,监视该图片,一旦生成了,则上传至七牛云.
        UploadTask task = new UploadTask(fileName, suffix);
        // 启动定时器
        Future future = taskScheduler.scheduleAtFixedRate(task, 500);
        // 结果传递给UploadTask
        task.setFuture(future);
    }

    class UploadTask implements Runnable {

        // 文件名称
        private String fileName;
        // 文件后缀
        private String suffix;
        // 启动任务的返回值
        private Future future;
        // 开始时间
        private long startTime;
        // 上传次数
        private int uploadTimes;

        public UploadTask(String fileName, String suffix) {
            this.fileName = fileName;
            this.suffix = suffix;
            this.startTime = System.currentTimeMillis();
        }

        public void setFuture(Future future) {
            this.future = future;
        }

        @Override
        public void run() {
            // 生成失败
            // 30s 内
            if (System.currentTimeMillis() - startTime > 30000) {
                log.error("执行时间过长,终止任务:" + fileName);
                future.cancel(true);
                return;
            }
            // 上传失败3次以上
            if (uploadTimes >= 3) {
                log.error("上传次数过多,终止任务:" + fileName);
                future.cancel(true);
                return;
            }

            String path = wkImageStorage + "/" + fileName + suffix;
            File file = new File(path);
            if (file.exists()) {
                log.info(String.format("开始第%d次上传[%s].", ++uploadTimes, fileName));
                // 设置响应信息
                StringMap policy = new StringMap();
                policy.put("returnBody", getJSONString(0));
                // 生成上传凭证
                Auth auth = Auth.create(accessKey, secretKey);
                String uploadToken = auth.uploadToken(shareBucketName, fileName, 3600, policy);
                // 指定上传机房
                UploadManager manager = new UploadManager(new Configuration(Zone.zone2()));
                try {
                    // 开始上传图片
                    Response response = manager.put(
                            path, fileName, uploadToken, null, "image/" + suffix, false);
                    // 处理响应结果
                    JSONObject json = JSONObject.parseObject(response.bodyString());
                    if (json == null || json.get("code") == null || !json.get("code").toString().equals("0")) {
                        log.info(String.format("第%d次上传失败[%s].", uploadTimes, fileName));
                    } else {
                        log.info(String.format("第%d次上传成功[%s].", uploadTimes, fileName));
                        future.cancel(true);
                    }
                } catch (QiniuException e) {
                    log.info(String.format("第%d次上传失败[%s].", uploadTimes, fileName));
                }
            } else {
                log.info("等待图片生成[" + fileName + "].");
            }
        }
    }
}
