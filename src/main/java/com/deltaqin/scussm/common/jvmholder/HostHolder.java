package com.deltaqin.scussm.common.jvmholder;

/**
 * @author deltaqin
 * @date 2021/6/24 上午12:15
 */

import com.deltaqin.scussm.entity.User;
import org.springframework.stereotype.Component;

/**
 * 持有用户信息,用于代替session对象.session也是线程隔离的
 *
 * 程序中不要频繁获取session。
 */
@Component
public class HostHolder {

    //
    private ThreadLocal<User> users = new ThreadLocal<>();

    public void setUser(User user) {
        // 存数据是按照线程来存的
        users.set(user);
    }

    public User getUser() {
        return users.get();
    }

    public void clear() {
        users.remove();
    }

}
