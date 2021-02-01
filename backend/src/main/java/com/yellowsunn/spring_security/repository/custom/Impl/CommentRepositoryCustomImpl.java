package com.yellowsunn.spring_security.repository.custom.Impl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.yellowsunn.spring_security.domain.entity.Board;
import com.yellowsunn.spring_security.domain.entity.Comment;
import com.yellowsunn.spring_security.repository.custom.CommentRepositoryCustom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityManager;
import java.util.List;

import static com.yellowsunn.spring_security.domain.entity.QComment.comment;

public class CommentRepositoryCustomImpl implements CommentRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public CommentRepositoryCustomImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Page<Comment> findCustomByBoard(Board board, Pageable pageable) {
        List<Comment> content = queryFactory.selectFrom(comment)
                .where(comment.board.eq(board))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(comment.orderNumber.asc())
                .fetch();

        long total = queryFactory.selectFrom(comment)
                .where(comment.board.eq(board))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchCount();

        return new PageImpl<>(content, pageable, total);
    }
}
