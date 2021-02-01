package com.yellowsunn.spring_security.service;

import com.yellowsunn.spring_security.domain.dto.CommentDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;

public interface CommentService {

    HttpStatus postComment(CommentDto commentDto);

    Page<CommentDto> findByPostId(Long postId, Pageable pageable);

    HttpStatus deleteByCommentId(Long commentId, String writer);
}
