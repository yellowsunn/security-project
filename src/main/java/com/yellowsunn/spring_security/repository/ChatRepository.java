package com.yellowsunn.spring_security.repository;

import com.yellowsunn.spring_security.domain.entity.Chat;
import com.yellowsunn.spring_security.repository.custom.ChatRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRepository extends JpaRepository<Chat, Long>, ChatRepositoryCustom {
}
