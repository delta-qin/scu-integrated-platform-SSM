package com.deltaqin.scussm.entity;

import lombok.Data;

import java.util.Date;

/**
 * @author deltaqin
 * @date 2021/6/24 上午12:30
 */
@Data
public class Message {

    private int id;
    private int fromId;
    private int toId;
    private String conversationId;
    private String content;
    private int status;
    private Date createTime;

}
