package com.deltaqin.scussm.entity;

import lombok.Data;

import java.util.Date;

/**
 * @author deltaqin
 * @date 2021/6/24 上午8:08
 */
@Data
public class LoginTicket {

    private int id;
    private int userId;
    private String ticket;
    private int status;
    private Date expired;
}
