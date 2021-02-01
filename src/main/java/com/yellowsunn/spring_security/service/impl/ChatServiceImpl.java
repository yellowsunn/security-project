package com.yellowsunn.spring_security.service.impl;

import com.yellowsunn.spring_security.domain.dto.ChatDto;
import com.yellowsunn.spring_security.domain.entity.Account;
import com.yellowsunn.spring_security.domain.entity.Chat;
import com.yellowsunn.spring_security.repository.AccountRepository;
import com.yellowsunn.spring_security.repository.ChatRepository;
import com.yellowsunn.spring_security.service.ChatService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {

    private final AccountRepository accountRepository;
    private final ChatRepository chatRepository;

    @Override
    public HttpStatus insert(ChatDto chatDto) {
        Optional<Account> accountOptional = accountRepository.findByUsername(chatDto.getWriter());
        if (accountOptional.isEmpty()) return HttpStatus.BAD_REQUEST;

        Chat chat = chatRepository.save(Chat.builder()
                .writer(chatDto.getWriter())
                .text(chatDto.getText())
                .build());

        long count = chatRepository.countByLocalDate(chat.createdDate);
        if (count == 1) {
            chat.updateDayFirst();
        }

        return HttpStatus.CREATED;
    }

    @Override
    public Page<ChatDto> findAll(Pageable pageable) {
        Page<Chat> chatPage = chatRepository.findCustomAll(pageable);

        return chatPage.map(chat ->
            ChatDto.builder()
                    .id(chat.getId())
                    .writer(chat.getWriter())
                    .date(chat.getCreatedDate())
                    .fullDate(chat.isDayFirst() ? chat.getCreatedDate() : null)
                    .text(chat.getText())
                    .isDayFirst(chat.isDayFirst())
                    .build()
        );
    }
}
