package com.yellowsunn.spring_security.repository;

import com.yellowsunn.spring_security.domain.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
}
