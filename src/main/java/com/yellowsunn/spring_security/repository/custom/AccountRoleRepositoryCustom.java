package com.yellowsunn.spring_security.repository.custom;

import com.yellowsunn.spring_security.domain.entity.Account;
import com.yellowsunn.spring_security.domain.entity.AccountRole;

import java.util.List;

public interface AccountRoleRepositoryCustom {

    List<AccountRole> findByAccount(Account account);
}
