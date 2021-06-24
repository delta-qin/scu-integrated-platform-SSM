package com.deltaqin.scussm.service;

/**
 * @author deltaqin
 * @date 2021/6/24 上午7:55
 */
public interface LikeService {
    // 点赞
    void like(int userId, int entityType, int entityId, int entityUserId);

    // 查询某实体点赞的数量
    long findEntityLikeCount(int entityType, int entityId);

    // 查询某人对某实体的点赞状态
    int findEntityLikeStatus(int userId, int entityType, int entityId);

    // 查询某个用户获得的赞
    int findUserLikeCount(int userId);
}
