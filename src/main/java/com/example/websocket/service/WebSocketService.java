package com.example.websocket.service;

import org.springframework.stereotype.Service;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

/**
 * @ServerEndPoint 注解是一个类层次的注解，它的功能主要是将目前的类定义成一个websocket服务器端，
 * 注解的值将被用于监听用户连接的终端访问URL地址，客户端可以通过这个URL连接到websocket服务器端
 * @username 是websocket客户端的唯一标识
 */
@ServerEndpoint("/websocket/{username}")
@Service
public class WebSocketService {
    private static int onlineCount = 0;
    private static CopyOnWriteArrayList<WebSocketService> webSocketSet = new CopyOnWriteArrayList<WebSocketService>();
    private Session session;
    private String username;

    @OnOpen
    public void onOpen(@PathParam("username") String username, Session session) {
        this.session = session;
        this.username = username;
        webSocketSet.add(this);//加入set中
        addOnlineCount();
        System.out.println("有新连接" + username + "加入！当前在线人数为" + getOnlineCount());
    }

    @OnClose
    public void onClose() {
        webSocketSet.remove(this);
        subOnlineCount();
        System.out.println("有一连接关闭！当前在线人数为" + getOnlineCount());
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        System.out.println("来自客户端的消息：" + message);
//        群发消息
//        for (WebSocketTest item:webSocketSet){
//            try {
//                item.sendMessage(message);
//            } catch (IOException e) {
//                e.printStackTrace();
//                continue;
//            }
//        }
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        System.out.println("发生错误！");
        throwable.printStackTrace();
    }

    //   下面是自定义的一些方法
    public void sendMessage(String message) throws IOException {
        this.session.getBasicRemote().sendText(message);
    }

    public void sendMessageToEveryone(String message) {
//        群发消息
        for (WebSocketService item : webSocketSet) {
            try {
                item.sendMessage(message);
            } catch (IOException e) {
                e.printStackTrace();
                continue;
            }
        }
    }

    public void sendMessageToSomeone(String message, String someone) {
//        群发消息
        for (WebSocketService item : webSocketSet) {
            if (someone.equals(item.username)) {
                try {
                    item.sendMessage(message);
                } catch (IOException e) {
                    e.printStackTrace();
                    continue;
                }
            }
        }
    }

    public static synchronized int getOnlineCount() {
        return onlineCount;
    }

    public static synchronized void addOnlineCount() {
        WebSocketService.onlineCount++;
    }

    public static synchronized void subOnlineCount() {
        WebSocketService.onlineCount--;
    }

    public static List<String> getAllClient() {
        return webSocketSet.stream().map(webSocketService -> webSocketService.username).collect(Collectors.toList());
    }
}