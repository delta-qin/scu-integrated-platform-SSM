package com.deltaqin.scussm.config;

import com.deltaqin.scussm.netty.NettyServer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class NettyConfig {

    @Bean
    public NettyServer createNettyServer() {
        NettyServer nettyServer = new NettyServer();

        //启动Netty服务，使用新的线程启动
        new Thread(){
            @Override
            public void run() {
               nettyServer.start(1234);
            }
        }.start();

        return nettyServer;
    }
}
