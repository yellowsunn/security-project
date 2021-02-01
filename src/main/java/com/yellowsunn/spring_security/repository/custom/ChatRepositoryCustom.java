package com.yellowsunn.spring_security.repository.custom;

import com.yellowsunn.spring_security.domain.entity.Chat;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;

public interface ChatRepositoryCustom {

    Page<Chat> findCustomAll(Pageable pageable);

    // 특정날짜의 개수를 셈
    long countByLocalDate(LocalDateTime localDateTime);
}
