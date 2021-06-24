package com.deltaqin.scussm.dao.elasticsearch;

import com.deltaqin.scussm.entity.DiscussPost;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

// 和关系型数据库类似
// ES也会有自己的数据访问层的接口
//Repository 是 spring的注解
@Repository
public interface DiscussPostRepository extends ElasticsearchRepository<DiscussPost, Integer> {

}
