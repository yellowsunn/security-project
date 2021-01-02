package com.yellowsunn.spring_security.repository.custom.Impl;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.yellowsunn.spring_security.domain.entity.Account;
import com.yellowsunn.spring_security.domain.entity.AccountRole;
import com.yellowsunn.spring_security.repository.custom.AccountRoleRepositoryCustom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

import static com.yellowsunn.spring_security.domain.entity.QAccountRole.accountRole;

@Transactional(readOnly = true)
public class AccountRoleRepositoryCustomImpl implements AccountRoleRepositoryCustom {

    private final JPAQueryFactory queryFactory;
    private final EntityManager em;

    public AccountRoleRepositoryCustomImpl(EntityManager em) {
        this.em = em;
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Optional<AccountRole> findByAccount(Account account) {
        AccountRole findAccountRole = queryFactory
                .selectFrom(accountRole)
                .join(accountRole.role).fetchJoin()
                .where(accountRole.account.eq(account))
                .fetchFirst();

        return Optional.ofNullable(findAccountRole);
    }

    @Override
    public List<AccountRole> findCustomAll() {
        return queryFactory.selectFrom(accountRole)
                .join(accountRole.role).fetchJoin()
                .join(accountRole.account).fetchJoin()
                .fetch();
    }

    @Override
    public Page<AccountRole> findBySearchCondition(String search, Pageable pageable) {
        QueryResults<AccountRole> results = queryFactory.selectFrom(accountRole)
                .join(accountRole.role).fetchJoin()
                .join(accountRole.account).fetchJoin()
                .where(startsWithUsername(search))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();

        List<AccountRole> content = results.getResults();
        long total = results.getTotal();

        return new PageImpl<>(content, pageable, total);
    }

    private BooleanExpression startsWithUsername(String search) {
        return StringUtils.hasText(search) ? accountRole.account.username.startsWith(search) : null ;
    }
}
