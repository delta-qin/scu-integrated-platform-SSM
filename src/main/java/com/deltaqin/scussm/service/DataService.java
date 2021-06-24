package com.deltaqin.scussm.service;

import java.util.Date;

/**
 * @author deltaqin
 * @date 2021/6/24 上午7:24
 */
public interface DataService {


    //将指定的IP计入UV
    void recordUV(String ip);

    // 统计指定日期范围内的UV
    long calculateUV(Date start, Date end);

    // 将指定用户计入DAU
    void recordDAU(int userId);

    // 统计指定日期范围内的DAU
    long calculateDAU(Date start, Date end);
}
