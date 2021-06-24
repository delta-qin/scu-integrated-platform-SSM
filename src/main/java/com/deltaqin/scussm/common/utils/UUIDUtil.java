package com.deltaqin.scussm.common.utils;

import java.util.UUID;

/**
 * @author deltaqin
 * @date 2021/6/24 上午9:47
 */
public class UUIDUtil {
    // 生成随机字符串
    public static String generateUUID() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

}
