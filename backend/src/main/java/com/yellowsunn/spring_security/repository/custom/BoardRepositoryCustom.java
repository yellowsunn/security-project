package com.yellowsunn.spring_security.repository.custom;

import com.yellowsunn.spring_security.domain.entity.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface BoardRepositoryCustom {

    Page<Board> findSimpleAll(String title, String username, Pageable pageable);

    Optional<Board> findCustomById(Long id);
}
