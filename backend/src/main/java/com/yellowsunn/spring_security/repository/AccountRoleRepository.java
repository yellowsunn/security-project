package com.yellowsunn.spring_security.repository;

import com.yellowsunn.spring_security.domain.entity.AccountRole;
import com.yellowsunn.spring_security.repository.custom.AccountRoleRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRoleRepository extends JpaRepository<AccountRole, Long>, AccountRoleRepositoryCustom {
}
