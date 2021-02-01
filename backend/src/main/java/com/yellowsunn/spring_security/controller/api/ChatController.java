package com.yellowsunn.spring_security.controller.api;

import com.yellowsunn.spring_security.domain.dto.ChatDto;
import com.yellowsunn.spring_security.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * 채팅 Controller (권한 - ROLE_MANAGER)
 */

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chat")
public class ChatController {

    private final ChatService chatService;

    // 채팅 입력
    @PostMapping("/insert")
    public ResponseEntity<?> insert(@RequestBody ChatDto chatDto) {
        HttpStatus httpStatus = chatService.insert(chatDto);
        return new ResponseEntity<>(httpStatus);
    }

    // 채팅 history
    @GetMapping("")
    public Page<ChatDto> list(@PageableDefault(size = 20) Pageable pageable) {
        return chatService.findAll(pageable);
    }
}
