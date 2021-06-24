package com.deltaqin.scussm;

import com.deltaqin.scussm.entity.Comment;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.List;

/**
 * @author deltaqin
 * @date 2021/6/24 下午5:24
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = SSMStartApplication.class)
public class MongoDB {


    @Autowired
    private MongoTemplate mongoTemplate;

    @Test
    public void testInsert() {


        for (int i = 0; i < 10 ; i++) {
            Comment comment = new Comment();


            comment.setEntityId(1);
            comment.setStatus(0);
            comment.setTargetId(0);
            comment.setUserId(1);
            comment.setEntityType(1);
            comment.setCreateTime(new Date());
            comment.setContent("sdsd" + i);
            Comment comment1 = mongoTemplate.insert(comment);
            System.out.println(comment.getId());
            System.out.println(comment1.getId());

        }


    }

    @Test
    public void select(){
        List<Comment> all = mongoTemplate.findAll(Comment.class);

        //List<Comment> all = commentRepository.findAll();
        all.forEach(item -> {
            System.out.println(item);
        });
    }

    @Test
    public void select1(){

        //List<Comment> all = commentRepository.findAllByEntityIdEqualsAndEntityTypeEquals(1, 1);
        //all.forEach(item -> {
        //    System.out.println(item);
        //});

        // 可以
        Query query = new Query();
        query.addCriteria(Criteria.where("entityId").is(1).and("entityType").is(1));
        long count = mongoTemplate.count(query, Comment.class);
        System.out.println(count);


        // 不可以
        //System.out.println("个数" + commentRepository.countAllByEntityTypeEqualsAndEntityIdEquals(1, 1));
    }

    @Test
    public void select2(){

        int currentPage = 1;
        int pageSize = 3;
        Query query = new Query();
        query.addCriteria(
                Criteria.where("entityId").is(1).and("entityType").is(1));
        int startIndex = ((currentPage - 1) < 0 ? 0:(currentPage - 1))*pageSize;
        query.skip(startIndex);
        query.limit(pageSize);


        query.with(Sort.by(
                Sort.Order.asc("createTime")
        ));

        List<Comment> commentV2s = mongoTemplate.find(query, Comment.class);

        commentV2s.forEach(item -> {
            System.out.println(item);
        });

    }


}
