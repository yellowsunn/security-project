package com.yellowsunn.spring_security.repository.custom.Impl;

import com.querydsl.core.QueryResults;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.yellowsunn.spring_security.domain.entity.Chat;
import com.yellowsunn.spring_security.repository.custom.ChatRepositoryCustom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityManager;

import java.time.LocalDateTime;

import static com.yellowsunn.spring_security.domain.entity.QChat.chat;

public class ChatRepositoryCustomImpl implements ChatRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public ChatRepositoryCustomImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Page<Chat> findCustomAll(Pageable pageable) {
        QueryResults<Chat> results = queryFactory.selectFrom(chat)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(chat.id.desc())
                .fetchResults();

        return new PageImpl<>(results.getResults(), pageable, results.getTotal());
    }

    @Override
    public long countByLocalDate(LocalDateTime localDateTime) {
        LocalDateTime from = LocalDateTime.of(localDateTime.getYear(), localDateTime.getMonth(), localDateTime.getDayOfMonth(), 0, 0);
        LocalDateTime to = LocalDateTime.of(localDateTime.getYear(), localDateTime.getMonth(), localDateTime.getDayOfMonth(), 23, 59, 59, 999999999);

        return queryFactory.selectFrom(chat)
                .where(chat.createdDate.between(from, to))
                .fetchCount();
    }
}
