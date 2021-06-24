package com.deltaqin.scussm.service;

import com.deltaqin.scussm.entity.Comment;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author deltaqin
 * @date 2021/6/24 上午7:21
 */
public interface CommentService {
    List<Comment> findCommentsByEntity(int entityType, int entityId, int offset, int limit);

    int findCommentCount(int entityType, int entityId);

    // 设置事务的隔离级别，以及事务的传播机制
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    int addComment(Comment comment);

    Comment findCommentById(int id);
}
