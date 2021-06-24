package com.deltaqin.scussm.config;

import com.deltaqin.scussm.interceptor.DataInterceptor;
import com.deltaqin.scussm.interceptor.LoginTicketInterceptor;
import com.deltaqin.scussm.interceptor.MessageInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author deltaqin
 * @date 2021/6/24 上午12:05
 */
// 配置拦截器生效

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {


    @Autowired
    private LoginTicketInterceptor loginTicketInterceptor;

    // 不使用这个，要使用spring security的
    //@Autowired
    //private LoginRequiredInterceptor loginRequiredInterceptor;

    @Autowired
    private MessageInterceptor messageInterceptor;

    @Autowired
    private DataInterceptor dataInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 这个拦截器不会拦截谁，会拦截谁
        // 静态资源不会拦截
        // 其余的不指定就是所有的被拦截，指定就是指定的被拦截
        //registry.addInterceptor(alphaInterceptor)
        //        .excludePathPatterns("/**/*.css", "/**/*.js", "/**/*.png", "/**/*.jpg", "/**/*.jpeg")
        //        .addPathPatterns("/register", "/login");

        // 不使用这个，要使用spring security的
        //        registry.addInterceptor(loginRequiredInterceptor)
        //                .excludePathPatterns("/**/*.css", "/**/*.js", "/**/*.png", "/**/*.jpg", "/**/*.jpeg");

        // 将用户的登录信息设置到处理当前请求的线程里面，方便使用，同时还会设置到security方便授权
        // 同时还会在请求处理结束之后将对应的ThreadLocal销毁防止内存泄漏
        registry.addInterceptor(loginTicketInterceptor)
                .excludePathPatterns("/**/*.css", "/**/*.js", "/**/*.png", "/**/*.jpg", "/**/*.jpeg");

        // 返回页面的时候查询一下当前的未读消息
        registry.addInterceptor(messageInterceptor)
                .excludePathPatterns("/**/*.css", "/**/*.js", "/**/*.png", "/**/*.jpg", "/**/*.jpeg");

        // 处理请求的时候统计网站的UV DAU
        registry.addInterceptor(dataInterceptor)
                .excludePathPatterns("/**/*.css", "/**/*.js", "/**/*.png", "/**/*.jpg", "/**/*.jpeg");
    }

}
