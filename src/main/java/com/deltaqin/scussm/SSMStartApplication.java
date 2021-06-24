package com.deltaqin.scussm;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;

/**
 * @author deltaqin
 * @date 2021/6/23 下午9:43
 */
@SpringBootApplication
@MapperScan("com.deltaqin.scussm.dao")
public class SSMStartApplication {
    public static void main(String[] args) {
        SpringApplication.run(SSMStartApplication.class, args);
    }

    @PostConstruct
    public void init() {
        // 解决Redis和ES依赖的 netty启动冲突问题
        // see Netty4Utils.setAvailableProcessors()
        System.setProperty("es.set.netty.runtime.available.processors", "false");
    }
}
