package com.yellowsunn.spring_security.repository;

import com.yellowsunn.spring_security.domain.entity.Board;
import com.yellowsunn.spring_security.repository.custom.BoardRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<Board, Long>, BoardRepositoryCustom {
}
