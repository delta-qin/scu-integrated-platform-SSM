
package com.deltaqin.scussm.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

// 配置spring 的线程池

@Configuration
@EnableScheduling
@EnableAsync
public class ThreadPoolConfig {
}
