package com.deltaqin.scussm.mq;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.deltaqin.scussm.common.CommunityConstant;
import com.deltaqin.scussm.entity.DiscussPost;
import com.deltaqin.scussm.service.DiscussPostService;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.*;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
//import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

/**
 * @author deltaqin
 * @date 2021/6/24 上午12:25
 */
@Component
public class MqProducer {
    //@Autowired
    //private KafkaTemplate kafkaTemplate;

    private DefaultMQProducer producer;

    // v3.0
    private TransactionMQProducer transactionMQProducer;



    @Value("${mq.nameserver.addr}")
    private String nameServerAddr;

    @Value("${mq.topicname}")
    private String topicName;


    // 事务消息使用，添加新的post
    @Autowired
    private DiscussPostService discussPostService;

    // 处理事件
    //public void fireEvent(Event event) {
    //    // 将事件发布到指定的主题
    //    kafkaTemplate.send(event.getTopic(), JSONObject.toJSONString(event));
    //}



    // 初始化方法
    @PostConstruct
    public void init() throws MQClientException {
        // 普通消息模式
        producer = new DefaultMQProducer(CommunityConstant.PRODUCER_GROUP);
        producer.setNamesrvAddr(nameServerAddr);
        producer.start();

        // 事务消息模式
        transactionMQProducer = new TransactionMQProducer(CommunityConstant.TRANSACTION_PRODUCER_GROUP);
        transactionMQProducer.setNamesrvAddr(nameServerAddr);
        transactionMQProducer.start();

        // 设置事务消息的相关回调（半消息设置）
        transactionMQProducer.setTransactionListener(new TransactionListener() {
            @Override
            public LocalTransactionState executeLocalTransaction(Message message, Object arg) {
                // 本地事务
                DiscussPost post = (DiscussPost) arg;

                try {
                    discussPostService.addDiscussPost(post);
                    //orderService.createOrder(userId, goodsId, secKillId, count, stockLogId);
                } catch (Exception e) {
                    e.printStackTrace();
                    return LocalTransactionState.ROLLBACK_MESSAGE;
                }
                return LocalTransactionState.COMMIT_MESSAGE;
            }

            @Override
            public LocalTransactionState checkLocalTransaction(MessageExt messageExt) {
                String string = new String(messageExt.getBody());

                DiscussPost post = JSON.parseObject(string, DiscussPost.class);

                DiscussPost discussPost = discussPostService.findDiscussPostById(post.getId());

                // 这里么有使用另外的Log记录，而是直接使用是否存在验证，因为上面的执行失败会直接回滚的
                //StockLog stockLog = stockLogMapper.selectByPrimaryKey(stockLogId);
                if (discussPost == null) {
                    return LocalTransactionState.UNKNOW;
                } else {
                    return LocalTransactionState.COMMIT_MESSAGE;
                }
            }
        });
    }

    // 非事务性 异步发送消息
    public boolean fireEvent(Event event) {
        String msg = JSONObject.toJSONString(event);

        Message message = new Message(topicName, "scu", msg.getBytes(Charset.forName("UTF-8")));

        try {
            producer.send(message);
        } catch (MQClientException e) {
            e.printStackTrace();
            return false;
        } catch (RemotingException e) {
            e.printStackTrace();
            return false;
        } catch (MQBrokerException e) {
            e.printStackTrace();
            return false;
        } catch (InterruptedException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    // 事务型 同步创建订单和异步扣减库存
    // 和上面的参数不一样，因为这个是在订单创建之前调用的，上面的是在订单创建之后调用的，上面只需简单发一个减数据库库存的消息即可
    public boolean transactionAsyncFireEvent(Event event, DiscussPost post) {

        String msg = JSONObject.toJSONString(event);
        Message message = new Message(topicName, "scu", msg.getBytes(Charset.forName("UTF-8")));

        TransactionSendResult result = null;
        try {
            result = transactionMQProducer.sendMessageInTransaction(message, post);
        } catch (MQClientException e) {
            e.printStackTrace();
            return false;
        }
        if (result.getLocalTransactionState() == LocalTransactionState.ROLLBACK_MESSAGE) {
            return false;
        } else if (result.getLocalTransactionState() == LocalTransactionState.COMMIT_MESSAGE) {
            return true;
        } else {
            return false;
        }

    }


}
