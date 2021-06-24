package com.deltaqin.scussm.config;

import com.deltaqin.scussm.common.CommunityConstant;
import com.deltaqin.scussm.common.utils.JSONStringUtil;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 根据用户是否登录、是否有权限决定处理方式
 *
 * 只限制需要限制的，不需要限制的直接在最后放行
 *
 * 没有登录的直接返回登录页面
 * 没有授权的提示权限不足
 *
 * 还要根据请求的类型返回对应的结果
 *
 *
 * 注意：登录认证还是使用我们自己的，授权使用Spring Security提供的
 * @author deltaqin
 * @date 2021/6/23 下午11:54
 */

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter implements CommunityConstant {

    // 忽略静态资源的拦截
    @Override
    public void configure(WebSecurity web) {
        web.ignoring().antMatchers("/resources/**");
    }

    // 授权
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // 这里的权限 LoginTicketInterceptor 里面设置的（存入SecurityContext,以便于Security进行授权.）
        http.authorizeRequests()
                .antMatchers(
                        "/user/setting",
                        "/user/upload",
                        "/discuss/add",
                        "/comment/add/**",
                        "/letter/**",
                        "/notice/**",
                        "/like",
                        "/follow",
                        "/unfollow"
                )
                // 上面的路径登录就可以请求
                .hasAnyAuthority(
                        AUTHORITY_USER,
                        AUTHORITY_ADMIN,
                        AUTHORITY_MODERATOR
                )
                .antMatchers(
                        "/discuss/top",
                        "/discuss/wonderful"
                )
                // 只有版主可以操作的路径
                .hasAnyAuthority(
                        AUTHORITY_MODERATOR
                )
                // 注意这里设置了监控的路径只有管理员可以访问
                .antMatchers(
                        "/discuss/delete",
                        "/data/**",
                        "/actuator/**"
                )
                // 只有管理员可以操作的路径
                .hasAnyAuthority(
                        AUTHORITY_ADMIN
                )
                //剩余的都允许
                .anyRequest().permitAll()
                // 开发阶段不会启用CSRF攻击防范
                .and().csrf().disable();

        // 认证授权异常 时的处理
        http.exceptionHandling()
                .authenticationEntryPoint(new AuthenticationEntryPoint() {
                    // 认证：没有登录时候怎么处理
                    @Override
                    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException, ServletException {
                        // 是请求JSON还是HTML
                        String xRequestedWith = request.getHeader("x-requested-with");
                        if ("XMLHttpRequest".equals(xRequestedWith)) {
                            // 异步请求，响应一个json字符，或者普通字符串
                            response.setContentType("application/plain;charset=utf-8");
                            PrintWriter writer = response.getWriter();
                            writer.write(JSONStringUtil.getJSONString(403, "你还没有登录哦!"));
                        } else {
                            response.sendRedirect(request.getContextPath() + "/login");
                        }
                    }
                })
                .accessDeniedHandler(new AccessDeniedHandler() {
                    // 授权：登录了但是 权限不足
                    @Override
                    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException e) throws IOException, ServletException {
                        String xRequestedWith = request.getHeader("x-requested-with");
                        if ("XMLHttpRequest".equals(xRequestedWith)) {
                            response.setContentType("application/plain;charset=utf-8");
                            PrintWriter writer = response.getWriter();
                            writer.write(JSONStringUtil.getJSONString(403, "你没有访问此功能的权限!"));
                        } else {
                            response.sendRedirect(request.getContextPath() + "/denied");
                        }
                    }
                });

        // Security底层默认会拦截/logout请求,进行退出处理. 是在servlet之前的、
        // 覆盖它默认的逻辑,才能执行我们自己的退出代码. 否则不会到达controller
        //相当于是拦截这个路径退出登录，但是我们不会请求这个，退出登录请求 就不会被拦截了
        http.logout().logoutUrl("/securitylogout");
    }

}

