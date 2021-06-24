package com.deltaqin.scussm.entity;

import lombok.Data;

import java.util.Date;

/**
 * @author deltaqin
 * @date 2021/6/24 上午12:15
 */
@Data
public class User {

    private int id;
    private String username;
    private String password;
    private String salt;
    private String email;
    private int type;
    private int status;
    private String activationCode;
    private String headerUrl;
    private Date createTime;

}
