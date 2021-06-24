package com.deltaqin.scussm.controller;

import com.deltaqin.scussm.common.CommunityConstant;
import com.deltaqin.scussm.common.jvmholder.HostHolder;
import com.deltaqin.scussm.common.utils.RedisKeyUtil;
import com.deltaqin.scussm.entity.Comment;
import com.deltaqin.scussm.entity.DiscussPost;
import com.deltaqin.scussm.mq.Event;
import com.deltaqin.scussm.mq.MqProducer;
import com.deltaqin.scussm.service.CommentService;
import com.deltaqin.scussm.service.DiscussPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Date;

/**
 * @author deltaqin
 * @date 2021/6/24 上午12:37
 */

@Controller
@RequestMapping("/comment")
public class CommentController implements CommunityConstant {

    @Autowired
    private CommentService commentService;

    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private MqProducer mqProducer;

    @Autowired
    private DiscussPostService discussPostService;

    @Autowired
    private RedisTemplate redisTemplate;

    @RequestMapping(path = "/add/{discussPostId}", method = RequestMethod.POST)
    public String addComment(@PathVariable("discussPostId") int discussPostId, Comment comment) {
        comment.setUserId(hostHolder.getUser().getId());
        comment.setStatus(0);
        comment.setCreateTime(new Date());
        commentService.addComment(comment);

        // 触发评论事件
        Event event = new Event()
                .setTopic(TOPIC_COMMENT)
                .setUserId(hostHolder.getUser().getId())
                .setEntityType(comment.getEntityType())
                .setEntityId(comment.getEntityId())
                .setData("postId", discussPostId);

        if (comment.getEntityType() == ENTITY_TYPE_POST) {
            // 评论的是帖子
            DiscussPost target = discussPostService.findDiscussPostById(comment.getEntityId());
            // 设置帖子的用户（通知这个用户）
            event.setEntityUserId(target.getUserId());
        } else if (comment.getEntityType() == ENTITY_TYPE_COMMENT) {
            // 评论的是评论
            Comment target = commentService.findCommentById(comment.getEntityId());
            // 设置评论被回复的用户（通知这个用户）
            event.setEntityUserId(target.getUserId());
        }
        // 发布到队列里面（削峰）
        mqProducer.fireEvent(event);

        if (comment.getEntityType() == ENTITY_TYPE_POST) {
            // 评论给帖子的时候，对应的分数是会变化的，这个需要反映到ES，会影响搜索的结果
            // 触发发帖事件
            event = new Event()
                    .setTopic(TOPIC_PUBLISH)
                    .setUserId(comment.getUserId())
                    .setEntityType(ENTITY_TYPE_POST)
                    .setEntityId(discussPostId);
            mqProducer.fireEvent(event);
            // 计算帖子分数
            String redisKey = RedisKeyUtil.getPostScoreKey();
            redisTemplate.opsForSet().add(redisKey, discussPostId);
        }

        return "redirect:/discuss/detail/" + discussPostId;
    }

}
