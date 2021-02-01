package com.yellowsunn.spring_security.repository.custom.Impl;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.yellowsunn.spring_security.domain.entity.Board;
import com.yellowsunn.spring_security.domain.entity.QBoard;
import com.yellowsunn.spring_security.repository.custom.BoardRepositoryCustom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;

import java.util.List;
import java.util.Optional;

import static com.yellowsunn.spring_security.domain.entity.QBoard.board;

public class BoardRepositoryCustomImpl implements BoardRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public BoardRepositoryCustomImpl(EntityManager entityManager) {
        queryFactory = new JPAQueryFactory(entityManager);
    }

    @Override
    public Page<Board> findSimpleAll(String title, String writer, Pageable pageable) {
        List<Board> content = queryFactory.selectFrom(board)
                .where(containsTitle(title), containsUsername(writer))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(board.id.desc())
                .fetch();

        long total = queryFactory.selectFrom(board)
                .where(containsTitle(title), containsUsername(writer))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchCount();

        return new PageImpl<>(content, pageable, total);
    }

    @Override
    public Optional<Board> findCustomById(Long id) {
        Board findBoard = queryFactory.selectFrom(board)
                .where(board.id.eq(id))
                .fetchOne();

        return Optional.ofNullable(findBoard);
    }

    private BooleanExpression containsUsername(String username) {
        return StringUtils.hasText(username) ? board.writer.contains(username) : null;
    }

    private BooleanExpression containsTitle(String title) {
        return StringUtils.hasText(title) ? board.title.contains(title) : null;
    }
}
