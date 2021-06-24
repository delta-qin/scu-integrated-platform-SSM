package com.deltaqin.scussm.service;

import com.deltaqin.scussm.entity.DiscussPost;
import org.springframework.data.domain.Page;

/**
 * @author deltaqin
 * @date 2021/6/24 上午7:41
 */
public interface ElasticsearchService {
    void saveDiscussPost(DiscussPost post);

    void deleteDiscussPost(int id);

    // Page是spring提供的，当前第几页，每一页多少个数据
    Page<DiscussPost> searchDiscussPost(String keyword, int current, int limit);
}
