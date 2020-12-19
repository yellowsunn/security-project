package com.yellowsunn.spring_security.repository;

import com.yellowsunn.spring_security.domain.entity.Role;
import com.yellowsunn.spring_security.domain.entity.RoleHierarchy;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class RoleHierarchyRepositoryTest {
    @Autowired RoleHierarchyRepository roleHierarchyRepository;
    @Autowired RoleRepository roleRepository;

    @Test
    void findByRole() {
        Role role = roleRepository.findByName("ROLE_ADMIN").get();
        RoleHierarchy roleHierarchy = roleHierarchyRepository.findByRole(role).get();

        List<Role> roles = new ArrayList<>();
        roles.add(roleHierarchy.getRole());

        RoleHierarchy parent = roleHierarchy.getParent();
        while (parent != null) {
            roles.add(parent.getRole());
            parent = parent.getParent();
        }

        List<String> roleNames = roles.stream().map(Role::getName).collect(Collectors.toList());
        assertThat(roleNames)
                .contains("ROLE_ADMIN", "ROLE_MANAGER", "ROLE_USER");
    }
}