package com.yellowsunn.spring_security;

import com.yellowsunn.spring_security.domain.entity.Account;
import com.yellowsunn.spring_security.domain.entity.AccountRole;
import com.yellowsunn.spring_security.domain.entity.Role;
import com.yellowsunn.spring_security.domain.entity.RoleHierarchy;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;

@Component
@RequiredArgsConstructor
public class InitDB {

    private final InitService initService;

    @PostConstruct
    public void init() {
        initService.dbInit();
    }

    @Component
    @Transactional
    @RequiredArgsConstructor
    static class InitService {

        private final EntityManager em;
        private final PasswordEncoder passwordEncoder;

        public void dbInit() {

            Role roleUser = Role.builder()
                    .name("ROLE_USER")
                    .build();

            Role roleManager = Role.builder()
                    .name("ROLE_MANAGER")
                    .build();

            Role roleAdmin = Role.builder()
                    .name("ROLE_ADMIN")
                    .build();

            Account root = Account.builder()
                    .username("root")
                    .password(passwordEncoder.encode("1111"))
                    .build();

            Account admin = Account.builder()
                    .username("admin")
                    .password(passwordEncoder.encode("1111"))
                    .build();

            Account manager = Account.builder()
                    .username("manager")
                    .password(passwordEncoder.encode("1111"))
                    .build();

            Account user = Account.builder()
                    .username("user")
                    .password(passwordEncoder.encode("1111"))
                    .build();

            em.persist(roleUser);
            em.persist(roleManager);
            em.persist(roleAdmin);
            em.persist(AccountRole.builder()
                    .account(root)
                    .role(roleAdmin)
                    .build());

            em.persist(AccountRole.builder()
                    .account(admin)
                    .role(roleAdmin)
                    .build());

            em.persist(AccountRole.builder()
                    .account(manager)
                    .role(roleManager)
                    .build());

            em.persist(AccountRole.builder()
                    .account(user)
                    .role(roleUser)
                    .build());

            // 계층구조 설정
            RoleHierarchy userHierarchy = RoleHierarchy.builder()
                    .role(roleUser)
                    .build();

            RoleHierarchy managerHierarchy = RoleHierarchy.builder()
                    .role(roleManager)
                    .parent(userHierarchy)
                    .build();

            RoleHierarchy adminHierarchy = RoleHierarchy.builder()
                    .role(roleAdmin)
                    .parent(managerHierarchy)
                    .build();

            em.persist(adminHierarchy);
        }
    }
}
