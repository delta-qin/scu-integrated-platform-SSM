package com.deltaqin.scussm.service;

import java.util.List;
import java.util.Map;

/**
 * @author deltaqin
 * @date 2021/6/24 上午7:48
 */
public interface FollowService {
    void follow(int userId, int entityType, int entityId);

    void unfollow(int userId, int entityType, int entityId);

    // 查询关注的实体的数量
    long findFolloweeCount(int userId, int entityType);

    // 查询实体的粉丝的数量
    long findFollowerCount(int entityType, int entityId);

    // 查询当前用户是否已关注该实体
    boolean hasFollowed(int userId, int entityType, int entityId);

    // 查询某用户关注的人
    List<Map<String, Object>> findFollowees(int userId, int offset, int limit);

    // 查询某用户的粉丝
    List<Map<String, Object>> findFollowers(int userId, int offset, int limit);
}
