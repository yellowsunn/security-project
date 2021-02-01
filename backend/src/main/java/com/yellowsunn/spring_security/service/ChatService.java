package com.yellowsunn.spring_security.service;

import com.yellowsunn.spring_security.domain.dto.ChatDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;

public interface ChatService {

    HttpStatus insert(ChatDto chatDto);

    Page<ChatDto> findAll(Pageable pageable);
}
