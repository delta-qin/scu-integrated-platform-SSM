package com.deltaqin.scussm.service;

import com.deltaqin.scussm.entity.Message;

import java.util.List;

/**
 * @author deltaqin
 * @date 2021/6/24 上午8:02
 */
public interface MessageService {
    // 查询当前用户的会话列表,针对每个会话只返回一条最新的私信.
    List<Message> findConversations(int userId, int offset, int limit);

    // 查询当前用户的会话数量
    int findConversationCount(int userId);

    //查询某个会话所包含的私信列表.
    List<Message> findLetters(String conversationId, int offset, int limit);

    // 查询某个会话所包含的私信数量.
    int findLetterCount(String conversationId);

    // 查询未读私信的数量
    int findLetterUnreadCount(int userId, String conversationId);

    int addMessage(Message message);

    int readMessage(List<Integer> ids);

    Message findLatestNotice(int userId, String topic);

    int findNoticeCount(int userId, String topic);

    int findNoticeUnreadCount(int userId, String topic);

    List<Message> findNotices(int userId, String topic, int offset, int limit);
}
