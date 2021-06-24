package com.deltaqin.scussm.service;

import com.deltaqin.scussm.entity.LoginTicket;
import com.deltaqin.scussm.entity.User;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.Map;

/**
 * @author deltaqin
 * @date 2021/6/24 上午8:06
 */
public interface UserService {
    User findUserById(int id);

    Map<String, Object> register(User user);

    int activation(int userId, String code);

    Map<String, Object> login(String username, String password, int expiredSeconds);

    void logout(String ticket);

    LoginTicket findLoginTicket(String ticket);

    int updateHeader(int userId, String headerUrl);

    User findUserByName(String username);

    Collection<? extends GrantedAuthority> getAuthorities(int userId);
}
