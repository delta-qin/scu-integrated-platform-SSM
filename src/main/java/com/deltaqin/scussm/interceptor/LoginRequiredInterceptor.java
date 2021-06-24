package com.deltaqin.scussm.interceptor;

import com.deltaqin.scussm.annotation.LoginRequired;
import com.deltaqin.scussm.common.jvmholder.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

/**
 * @author deltaqin
 * @date 2021/6/24 上午12:14
 */

@Component
public class LoginRequiredInterceptor implements HandlerInterceptor {

    @Autowired
    private HostHolder hostHolder;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 只是处理方法，不会处理其他的。所以其他的直接返回true
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            Method method = handlerMethod.getMethod();
            // 按照指定到类型获取注解
            LoginRequired loginRequired = method.getAnnotation(LoginRequired.class);
            //意味着是需要登录的
            if (loginRequired != null && hostHolder.getUser() == null) {
                // 请求里面 直接获取应用路径，返回要登录的页面
                response.sendRedirect(request.getContextPath() + "/login");
                return false;
            }
        }
        return true;
    }
}

