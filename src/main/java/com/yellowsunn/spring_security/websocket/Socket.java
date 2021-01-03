package com.yellowsunn.spring_security.websocket;

import com.google.gson.Gson;
import com.yellowsunn.spring_security.domain.dto.SyncDto;
import com.yellowsunn.spring_security.domain.dto.UserDto;
import com.yellowsunn.spring_security.domain.dto.WebsocketDto;
import com.yellowsunn.spring_security.service.UserService;
import com.yellowsunn.spring_security.websocket.config.CustomSpringConfigurator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Component
@ServerEndpoint(value = "/websocket", configurator = CustomSpringConfigurator.class)
@RequiredArgsConstructor
public class Socket {

    private Set<Session> sessions = new HashSet<>();
    private final UserService userService;

    @OnOpen
    public void onOpen(Session session) {
        sessions.add(session);
        log.info("onOpen called, userCount: " + sessions.size());
    }

    @OnClose //클라이언트와 소켓과의 연결이 닫힐때 (끊길떄) 마다 호출
    public void onClose(Session session) {
        sessions.remove(session);
        log.info("onClose called, userCount:" + sessions.size());
    }

    @OnMessage
    public void onMessage(String message) {
        log.info("onMessage called, message: " + message);

        Gson gson = new Gson();
        SyncDto syncDto = gson.fromJson(message, SyncDto.class);

        String data = null;
        if (syncDto.isChanged()) {
            Optional<UserDto> userDtoOptional = userService.findByUsername(syncDto.getUsername());
            if (userDtoOptional.isPresent()) {
                UserDto userDto = userDtoOptional.get();
                WebsocketDto websocketDto = new WebsocketDto();
                websocketDto.setUser(userDto);
                websocketDto.setChanged(true);
                data = gson.toJson(websocketDto);
            }
        } else if (syncDto.isDeleted()) {
            WebsocketDto websocketDto = new WebsocketDto();
            websocketDto.setUsername(syncDto.getUsername());
            websocketDto.setDeleted(true);
            data = gson.toJson(websocketDto);
        }

        if (data != null) {
            for (Session session : sessions) {
                sendMessage(session, data);
            }
        }
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        log.warn("onClose called, error: " + throwable.getMessage());
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
