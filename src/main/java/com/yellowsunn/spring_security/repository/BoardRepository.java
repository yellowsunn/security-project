package com.yellowsunn.spring_security.repository;

import com.yellowsunn.spring_security.domain.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<Board, Long> {
}
