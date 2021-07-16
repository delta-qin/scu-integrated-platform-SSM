package com.deltaqin.scussm;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

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
    //
    //@Override
    //public void addResourceHandlers(ResourceHandlerRegistry registry) {
    //    //registry.addResourceHandler("doc.html").addResourceLocations("classpath:/META-INF/resources/");
    //    // https://blog.csdn.net/qq_38380025/article/details/84936466
    //    // 解决找不到图标，添加一个资源处理器来处理文件和位置的映射
    //    registry.addResourceHandler("/favicon.ico").addResourceLocations("classpath:/static/");
    //    //registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
    //}
}
