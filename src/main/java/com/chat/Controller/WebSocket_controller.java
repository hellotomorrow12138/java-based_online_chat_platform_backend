package com.chat.Controller;
import com.alibaba.fastjson2.JSON;
import com.chat.Model.Message;
import com.chat.Model.UserList;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
@Component
@ServerEndpoint("/socket/{username}/{roomId}")
public class WebSocket_controller {
    public static final Map<String, Session> sessionMap = new ConcurrentHashMap<>();
    public static final Map<String, Set<String>> roomUserMap = new ConcurrentHashMap<>();
    private void sendHistoryMessagesToUser(String username, String roomId) {
        try (Jedis jedis = new Jedis("127.0.0.1", 6379)) {
            String chatListKey = "chat_list:" + roomId;
            List<String> historyMessages = jedis.lrange(chatListKey, 0, 30); // 获取列表中30条记录
            // 确保用户已经加入房间并且Session有效
            if (roomUserMap.containsKey(roomId) && sessionMap.containsKey(username)) {
                Session session = sessionMap.get(username);
                if (session != null && session.isOpen()) {
                    for (String messageJson : historyMessages) {
                        // 发送每条历史消息到新用户的WebSocket会话
                        session.getBasicRemote().sendText(messageJson);
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("获取Redis聊天记录失败：" + e.getMessage());
        }
    }
    @OnOpen
    public void onOpen(Session session,
                       @PathParam("username") String username,
                       @PathParam("roomId") String roomId) {
        sessionMap.put(username, session);
        roomUserMap.computeIfAbsent(roomId, k -> ConcurrentHashMap.newKeySet()).add(username);
        sendRoomMessage(roomId, setUserList(roomId)); // 只向同一房间的用户广播加入消息
        sendHistoryMessagesToUser(username, roomId);
    }
    private String setUserList(String roomId) {
        Set<String> userList = roomUserMap.getOrDefault(roomId, Collections.emptySet());
        ArrayList<String> userListAsList = new ArrayList<>(userList);
        UserList userListObj = new UserList();
        userListObj.setUserlist(userListAsList);
//        System.out.println(JSON.toJSONString(userListObj));
        return JSON.toJSONString(userListObj);
    }
    @OnMessage
    public void onMessage(String message) {
        try {
            Message msg = JSON.parseObject(message, Message.class);
            String roomId = msg.getRoomId();
//            System.out.println(JSON.toJSONString(msg));
            if (roomId != null) {
                sendRoomMessage(roomId, JSON.toJSONString(msg));
                // 直接实例化Jedis并存储数据到Redis列表
                try (Jedis jedis = new Jedis("127.0.0.1", 6379)) {
                    String chatListKey = "chat_list:" + roomId;
                    jedis.rpush(chatListKey, JSON.toJSONString(msg)); // 将消息添加到列表的尾部
//                    System.out.println("对话消息存储到Redis列表成功");
                } catch (Exception e) {
                    System.err.println("连接Redis失败：" + e.getMessage());
                }

            } else {
                System.err.println("Received message with no roomId.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void sendRoomMessage(String roomId, String message) {
        Set<String> usersInRoom = roomUserMap.get(roomId);
        if (usersInRoom != null) {
            for (String username : usersInRoom) {
                Session session = sessionMap.get(username);
                if (session != null && session.isOpen()) {
                    try {
                        session.getBasicRemote().sendText(message);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
    @OnClose
    public void onClose(@PathParam("username") String username, @PathParam("roomId") String roomId) {
        sessionMap.remove(username);
        roomUserMap.get(roomId).remove(username);
        sendRoomMessage(roomId, setUserList(roomId));
//        System.out.println(username+"已离开");
    }
    @OnError
    public void onError(Throwable error) {
        error.printStackTrace();
    }
}
