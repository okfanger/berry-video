package cn.akfang.berry.ws;

import cn.akfang.berry.manager.InteractManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

@ServerEndpoint("/ws/{roomId}")
@Component
@Slf4j
public class InteractServer {

    @Autowired
    InteractManager interactManager;
    //与某个客户端的连接会话，需要通过它来给客户端发送数据
    private Session session;
    /**
     * 用户ID
     */
    private String userId;

    private String roomId;
    private static ConcurrentHashMap<String, InteractServer> webSocketsMap = new ConcurrentHashMap<>();
    private static ConcurrentHashMap<String, Session> sessionsMap = new ConcurrentHashMap<String, Session>();


    @OnOpen
    public void onOpen(Session session, @PathParam("roomId") String roomId) {
        log.info("ws onOpen entry: roomId={}", roomId);
        this.roomId = roomId;
        this.session = session;
        try {
            webSocketsMap.put(roomId, this);
            sessionsMap.put(roomId, session);
            log.info("ws onOpen success: roomId={}", roomId);
        } catch (Exception e) {
            log.error("ws connect exception，roomId={}，e={}", roomId, e.getMessage());
        }
    }

    @OnClose
    public void onClose() {
        log.info("ws onClose entry: roomId={}", this.roomId);
        try {
            webSocketsMap.remove(this.roomId);
            sessionsMap.remove(this.roomId);
            log.info("ws onClose: roomId={}", this.roomId);
        } catch (Exception e) {
            log.error("ws onClose exception，roomId={}，e={}", this.roomId, e.getMessage());
        }
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        log.info("ws onMessage: message={}", message);
    }

    @OnError
    public void onError(Session session, Throwable error) {
        log.info("ws onError: error={}", error.getMessage());
    }

    /**
     * 实现服务器主动推送
     */
    public void sendMessage(String message) throws IOException {
        log.info("ws sendMessage: message={}", message);
        this.session.getBasicRemote().sendText(message);
    }

    /**
     * 群发自定义消息
     */
    public static void sendMqMessage(String message) throws IOException {
        log.info("ws sendMqMessage: message={}", message);
    }

    /**
     * 群发自定义消息
     */
    public static void sendInfo(String message, @PathParam("from") String from) throws IOException {

    }
}