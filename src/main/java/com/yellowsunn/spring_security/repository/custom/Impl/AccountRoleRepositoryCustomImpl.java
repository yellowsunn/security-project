package com.yellowsunn.spring_security.repository.custom.Impl;

import com.yellowsunn.spring_security.domain.entity.Account;
import com.yellowsunn.spring_security.domain.entity.AccountRole;
import com.yellowsunn.spring_security.repository.custom.AccountRoleRepositoryCustom;
import lombok.RequiredArgsConstructor;

import javax.persistence.EntityManager;
import java.util.List;

@RequiredArgsConstructor
public class AccountRoleRepositoryCustomImpl implements AccountRoleRepositoryCustom {

    private final EntityManager em;

    @Override
    public List<AccountRole> findByAccount(Account account) {
        return em.createQuery("select ar from AccountRole ar join fetch ar.role r", AccountRole.class)
                .getResultList();
    }
}
