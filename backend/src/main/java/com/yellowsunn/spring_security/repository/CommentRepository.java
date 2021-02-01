package com.yellowsunn.spring_security.repository;

import com.yellowsunn.spring_security.domain.entity.Comment;
import com.yellowsunn.spring_security.repository.custom.CommentRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long>, CommentRepositoryCustom {
}
