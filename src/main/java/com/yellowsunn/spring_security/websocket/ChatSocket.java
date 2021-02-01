package com.yellowsunn.spring_security.websocket;

import com.google.gson.Gson;
import com.yellowsunn.spring_security.domain.dto.ChatDto;
import com.yellowsunn.spring_security.service.ChatService;
import com.yellowsunn.spring_security.websocket.config.CustomSpringConfigurator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * 채팅 페이지에서 사용되는 웹소켓
 */
@Slf4j
@Component
@ServerEndpoint(value = "/chat", configurator = CustomSpringConfigurator.class)
@RequiredArgsConstructor
public class ChatSocket {

    private Set<Session> sessions = new HashSet<>();
    private Gson gson = new Gson();
    private final ChatService chatService;

    @OnOpen
    public void onOpen(Session session) {
        sessions.add(session);
    }

    @OnClose
    public void onClose(Session session) {
        sessions.remove(session);
    }

    @OnMessage
    public void onMessage(String message) throws Exception {
        log.info(message);
        ChatDto chatDto = gson.fromJson(message, ChatDto.class);
        HttpStatus httpStatus = chatService.insert(chatDto);

        if (httpStatus != HttpStatus.CREATED) {
            throw new Exception("Failed to insert chat message.");
        }

        for (Session session : sessions) {
            sendMessage(session, String.valueOf(HttpStatus.CREATED.value()));
        }
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        log.warn("error: " + throwable.getMessage());
        sessions.remove(session);
    }

    private void sendMessage(Session session, String message) {
        try {
            session.getBasicRemote().sendText(message);
        } catch (IOException e) {
            log.warn("Caught exception while sending message to Session "
                    + session.getId() + "error: " + e.getMessage());
        }
    }
}
