package com.yellowsunn.spring_security.repository;

import com.yellowsunn.spring_security.domain.entity.Account;
import com.yellowsunn.spring_security.domain.entity.AccountRole;
import com.yellowsunn.spring_security.repository.custom.AccountRoleRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AccountRoleRepository extends JpaRepository<AccountRole, Long>, AccountRoleRepositoryCustom {


}
