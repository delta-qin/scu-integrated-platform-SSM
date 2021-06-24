package com.deltaqin.scussm.entity;

import lombok.Data;

import java.util.Date;

/**
 * @author deltaqin
 * @date 2021/6/24 上午12:33
 */
@Data
public class Comment {

    private int id;
    private int userId;
    // 类型包含了文章的评论和评论的回复
    private int entityType;
    private int entityId;
    // 是0 就表示评论的是文章，如果是回复的话就会有对应的用户ID
    private int targetId;

    private String content;
    private int status;
    private Date createTime;
}
