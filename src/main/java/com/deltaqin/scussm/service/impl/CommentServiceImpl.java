package com.deltaqin.scussm.service.impl;

import com.deltaqin.scussm.common.CommunityConstant;
import com.deltaqin.scussm.common.utils.SensitiveFilter;
import com.deltaqin.scussm.dao.CommentMapper;
import com.deltaqin.scussm.entity.Comment;
import com.deltaqin.scussm.service.CommentService;
import com.deltaqin.scussm.service.DiscussPostService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
public class CommentServiceImpl implements CommentService, CommunityConstant {


    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private SensitiveFilter sensitiveFilter;

    @Autowired
    private DiscussPostService discussPostService;

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
        return commentMapper.selectCommentsByEntity(entityType, entityId, offset, limit);
    }

    @Override
    public int findCommentCount(int entityType, int entityId) {
        return commentMapper.selectCountByEntity(entityType, entityId);
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
        int rows = commentMapper.insertComment(comment);

        // 更新帖子评论数量
        if (comment.getEntityType() == ENTITY_TYPE_POST) {
            int count = commentMapper.selectCountByEntity(comment.getEntityType(), comment.getEntityId());
            discussPostService.updateCommentCount(comment.getEntityId(), count);
        }

        return rows;
    }

    @Override
    public Comment findCommentById(int id) {
        return commentMapper.selectCommentById(id);
    }


}
