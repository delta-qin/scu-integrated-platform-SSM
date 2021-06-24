package com.deltaqin.scussm.interceptor;

import com.deltaqin.scussm.common.jvmholder.HostHolder;
import com.deltaqin.scussm.common.utils.CookieUtil;
import com.deltaqin.scussm.entity.LoginTicket;
import com.deltaqin.scussm.entity.User;
import com.deltaqin.scussm.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * 借助拦截器的三个生效的位置，将ThreadLocal 结合 Model View Controller 有机结合在一起，方便了用户信息的获取
 *
 * 请求到来时候如果有登陆凭据在cookie里面，将用户的登陆信息放到线程ThreadLocal里面，方便后面使用
 *
 * 返回Model给DispatcherSerlet的时候需要将用户的登陆信息设置到Model里面，模板使用其判断和显示用户的相关信息
 *
 * 最后生成view要返回的时候将线程里面的ThreadLocal销毁防止内存泄漏
 *
 * @author deltaqin
 */
@Component
public class LoginTicketInterceptor implements HandlerInterceptor {

    @Autowired
    private UserService userService;

    @Autowired
    private HostHolder hostHolder;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 从cookie中获取凭证
        String ticket = CookieUtil.getValue(request, "ticket");

        if (ticket != null) {
            // 查询凭证从Redis里面
            LoginTicket loginTicket = userService.findLoginTicket(ticket);
            // 检查凭证是否有效
            if (loginTicket != null && loginTicket.getStatus() == 0 && loginTicket.getExpired().after(new Date())) {
                // 根据凭证查询用户（也是缓存里面）
                User user = userService.findUserById(loginTicket.getUserId());
                // 在本次请求中持有用户「线程处理请求」
                hostHolder.setUser(user);

                // 下面是spring Security 需要使用的
                // 构建用户认证的结果,并存入SecurityContext,以便于Security进行授权.
                Authentication authentication = new UsernamePasswordAuthenticationToken(
                        user, user.getPassword(), userService.getAuthorities(user.getId()));
                // 退出登录的时候也要配置
                SecurityContextHolder.setContext(new SecurityContextImpl(authentication));
            }
        }

        return true;
    }

    // 这个是为了模板可以使用处理好的值
    // 头部在模板里面会根据是否有数据合理展现。th:if 决定是否展示
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        User user = hostHolder.getUser();

        // 没有登录的话就直接不会设置Model里面的值，模板就不会渲染对应的只有用户登录才可以看到的值
        if (user != null && modelAndView != null) {
            modelAndView.addObject("loginUser", user);
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        hostHolder.clear();
        SecurityContextHolder.clearContext();
    }
}
