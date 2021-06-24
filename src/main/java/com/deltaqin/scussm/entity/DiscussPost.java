package com.deltaqin.scussm.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.Date;

/**
 * @author deltaqin
 * @date 2021/6/24 上午12:32
 */

// 不需要像mybatis写xml，直接在类上注解

// 实体自动和索引字段映射

// type在当前版本写作固定的，7里面会废弃
// 分片：自动创建
// 副本：自动创建
@Document(indexName = "discusspost", type = "_doc", shards = 6, replicas = 3)
//文档就是行
@Data
public class DiscussPost {

    @Id
    private int id;

    // Field就是ES的字段
    @Field(type = FieldType.Integer)
    private int userId;

    // 互联网校招
    // 保存的时候分词器尽可能增加更多的分词器 ik_max_word  范围非常大
    // 搜索的时候使用聪明的分词器而不是最大的
    @Field(type = FieldType.Text, analyzer = "ik_max_word", searchAnalyzer = "ik_smart")
    private String title;

    @Field(type = FieldType.Text, analyzer = "ik_max_word", searchAnalyzer = "ik_smart")
    private String content;

    // 0-普通; 1-置顶;
    @Field(type = FieldType.Integer)
    private int type;

    // 0 未读， 1已读， 2 删除
    @Field(type = FieldType.Integer)
    private int status;

    @Field(type = FieldType.Date)
    private Date createTime;

    @Field(type = FieldType.Integer)
    private int commentCount;

    @Field(type = FieldType.Double)
    private double score;
}
