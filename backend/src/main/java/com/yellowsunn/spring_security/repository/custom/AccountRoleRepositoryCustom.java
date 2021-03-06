package com.yellowsunn.spring_security.repository.custom;

import com.yellowsunn.spring_security.domain.entity.Account;
import com.yellowsunn.spring_security.domain.entity.AccountRole;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface AccountRoleRepositoryCustom {

    Optional<AccountRole> findByAccount(Account account);

    List<AccountRole> findCustomAll();

    Page<AccountRole> findBySearchCondition(String search, Pageable pageable);
}
