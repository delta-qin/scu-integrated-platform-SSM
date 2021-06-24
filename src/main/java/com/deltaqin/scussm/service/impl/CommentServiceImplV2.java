package com.deltaqin.scussm.service.impl;

import com.deltaqin.scussm.common.CommunityConstant;
import com.deltaqin.scussm.common.utils.SensitiveFilter;
import com.deltaqin.scussm.entity.Comment;
import com.deltaqin.scussm.service.CommentService;
import com.deltaqin.scussm.service.DiscussPostService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.HtmlUtils;

import java.util.List;

/**
 * @author deltaqin
 * @date 2021/6/24 上午7:21
 */
@Slf4j
@Service
@Primary
public class CommentServiceImplV2 implements CommentService, CommunityConstant {


    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private SensitiveFilter sensitiveFilter;

    @Autowired
    private DiscussPostService discussPostService;

    @Override
    public Comment findCommentById(int id) {
        //return commentMapper.selectCommentById(id);
        return mongoTemplate.findById(id, Comment.class);
    }



    /**
     * 获取某个类型文章（推文，帖子，朋友圈）的对应ID的所有评论
     * @param entityType 评论的类型
     * @param entityId 评论的对象的ID
     * @param offset 页偏移
     * @param limit 一页的大小
     * @return
     */
    @Override
    public List<Comment> findCommentsByEntity(int entityType, int entityId, int offset, int limit) {
        //return commentMapper.selectCommentsByEntity(entityType, entityId, offset, limit);

        Query query = new Query();
        query.addCriteria(
                Criteria.where("entityId").is(entityId).and("entityType").is(entityType));
        int startIndex = ((offset - 1) < 0 ? 0:(offset - 1))*limit;
        query.skip(startIndex);
        query.limit(limit);


        query.with(Sort.by(
                Sort.Order.asc("createTime")
        ));

        List<Comment> commentV2s = mongoTemplate.find(query, Comment.class);

        //commentV2s.forEach(item -> {
        //    System.out.println(item);
        //});

        return commentV2s;
    }

    @Override
    public int findCommentCount(int entityType, int entityId) {
        //return commentMapper.selectCountByEntity(entityType, entityId);
        Query query = new Query();
        query.addCriteria(Criteria.where("entityId").is(entityId).and("entityType").is(entityType));
        long count = mongoTemplate.count(query, Comment.class);
        return new Long(count).intValue();
    }

    @Override
    // 设置事务的隔离级别，以及事务的传播机制
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    public int addComment(Comment comment) {
        if (comment == null) {
            throw new IllegalArgumentException("参数不能为空!");
        }

        // 添加评论
        comment.setContent(HtmlUtils.htmlEscape(comment.getContent()));
        comment.setContent(sensitiveFilter.filter(comment.getContent()));

        mongoTemplate.save(comment);

        //int rows = commentMapper.insertComment(comment);


        // 更新帖子评论数量
        if (comment.getEntityType() == ENTITY_TYPE_POST) {
            Query query = new Query();
            query.addCriteria(Criteria.where("entityId").is(comment.getEntityId()).and("entityType").is(comment.getEntityType()));
            long count = mongoTemplate.count(query, Comment.class);
            //int count = commentMapper.selectCountByEntity(comment.getEntityType(), comment.getEntityId());
            discussPostService.updateCommentCount(comment.getEntityId(), new Long(count).intValue());
        }

        return 1;
    }

    public void deleteById(int id) {
        //commentRepository.deleteById("" + id);
        // 不是真的删除，只是设置一下状态
        Comment comment = mongoTemplate.findById(id, Comment.class);
        comment.setStatus(1);
        mongoTemplate.save(comment);
    }

}
