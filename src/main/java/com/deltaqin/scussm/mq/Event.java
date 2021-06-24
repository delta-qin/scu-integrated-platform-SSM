package com.deltaqin.scussm.mq;

import java.util.HashMap;
import java.util.Map;

/**
 * @author deltaqin
 * @date 2021/6/24 上午12:27
 */
public class Event {

    // 事件类型
    private String topic;
    //触发人
    private int userId;
    //事件发生在哪一个实体
    private int entityType;
    // 实体的id
    private int entityId;
    // 实体属于谁
    private int entityUserId;
    // 有扩展性，额外的数据直接map
    private Map<String, Object> data = new HashMap<>();

    public String getTopic() {
        return topic;
    }

    // 所有set 直接返回Event，就可以实现链式编程
    public Event setTopic(String topic) {
        this.topic = topic;
        return this;
    }

    public int getUserId() {
        return userId;
    }

    public Event setUserId(int userId) {
        this.userId = userId;
        return this;
    }

    public int getEntityType() {
        return entityType;
    }

    public Event setEntityType(int entityType) {
        this.entityType = entityType;
        return this;
    }

    public int getEntityId() {
        return entityId;
    }

    public Event setEntityId(int entityId) {
        this.entityId = entityId;
        return this;
    }

    public int getEntityUserId() {
        return entityUserId;
    }

    public Event setEntityUserId(int entityUserId) {
        this.entityUserId = entityUserId;
        return this;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public Event setData(String key, Object value) {
        this.data.put(key, value);
        return this;
    }

}
