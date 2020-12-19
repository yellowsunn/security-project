package com.yellowsunn.spring_security.repository.custom.Impl;

import com.yellowsunn.spring_security.domain.entity.Account;
import com.yellowsunn.spring_security.domain.entity.AccountRole;
import com.yellowsunn.spring_security.repository.custom.AccountRoleRepositoryCustom;
import lombok.RequiredArgsConstructor;

import javax.persistence.EntityManager;
import java.util.Optional;

@RequiredArgsConstructor
public class AccountRoleRepositoryCustomImpl implements AccountRoleRepositoryCustom {

    private final EntityManager em;

    @Override
    public Optional<AccountRole> findByAccount(Account account) {
        AccountRole accountRole = em.createQuery(
                "select ar from AccountRole ar " +
                        "join fetch ar.role r " +
                        "where ar.account = :account", AccountRole.class)
                .setParameter("account", account)
                .getSingleResult();

        return Optional.ofNullable(accountRole);
    }
}
