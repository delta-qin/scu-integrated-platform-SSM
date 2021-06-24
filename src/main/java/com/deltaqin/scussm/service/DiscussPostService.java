package com.deltaqin.scussm.service;

import com.deltaqin.scussm.entity.DiscussPost;

import java.util.List;

/**
 * @author deltaqin
 * @date 2021/6/24 上午7:37
 */
public interface DiscussPostService {
    List<DiscussPost> findDiscussPosts(int userId, int offset, int limit, int orderMode);

    int findDiscussPostRows(int userId);

    int addDiscussPost(DiscussPost post);

    DiscussPost findDiscussPostById(int id);

    int updateCommentCount(int id, int commentCount);

    int updateType(int id, int type);

    int updateStatus(int id, int status);

    int updateScore(int id, double score);
}
