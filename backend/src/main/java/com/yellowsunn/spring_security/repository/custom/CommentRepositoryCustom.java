package com.yellowsunn.spring_security.repository.custom;

import com.yellowsunn.spring_security.domain.entity.Board;
import com.yellowsunn.spring_security.domain.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CommentRepositoryCustom {

    Page<Comment> findCustomByBoard(Board board, Pageable pageable);
}
