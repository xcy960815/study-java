package com.example.websocket;

//import javax.websocket.*;
//import javax.websocket.server.ServerEndpoint;

import com.mysql.cj.Session;
import org.springframework.stereotype.Component;

//@Component
//@ServerEndpoint("/ws/chat")  // WebSocket 访问路径
public class WebSocket {

//    @OnOpen
//    public void onOpen(Session session) {
//        System.out.println("New connection: " + session.getId());
//        // 可以在此处进行用户连接成功后的操作
//    }
//
//    @OnMessage
//    public void onMessage(String message, Session session) {
//        System.out.println("Received message from " + session.getId() + ": " + message);
//        // 可以在这里实现消息的处理和广播
//        sendMessageToClient(session, "Message received: " + message);
//    }
//
//    @OnClose
//    public void onClose(Session session) {
//        System.out.println("Connection closed: " + session.getId());
//        // 在这里可以进行关闭连接的处理
//    }
//
//    @OnError
//    public void onError(Session session, Throwable throwable) {
//        System.out.println("Error on connection " + session.getId() + ": " + throwable.getMessage());
//        // 在这里可以实现错误处理逻辑
//    }
//
//    private void sendMessageToClient(Session session, String message) {
//        try {
//            session.getBasicRemote().sendText(message);
//        } catch (Exception e) {
//            System.out.println("Failed to send message: " + e.getMessage());
//        }
//    }
}
