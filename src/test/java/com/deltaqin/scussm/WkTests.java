package com.deltaqin.scussm;

import java.io.IOException;

public class WkTests {

    public static void main(String[] args) {
        String cmd = "/usr/local/bin/wkhtmltoimage --quality 75 https://www.nowcoder.com ~/Desktop/4.png";
        try {
            // 只是提交，不会等待OS返回，直接结束
            Runtime.getRuntime().exec(cmd);
            System.out.println("ok.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
