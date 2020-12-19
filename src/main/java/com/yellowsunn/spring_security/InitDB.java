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
            Account account = Account.builder()
                    .username("admin")
                    .password(passwordEncoder.encode("1111"))
                    .build();

            Role roleUser = Role.builder()
                    .name("ROLE_USER")
                    .build();

            Role roleManager = Role.builder()
                    .name("ROLE_MANAGER")
                    .build();

            Role roleAdmin = Role.builder()
                    .name("ROLE_ADMIN")
                    .build();

            em.persist(roleUser);
            em.persist(roleManager);
            em.persist(roleAdmin);
            em.persist(AccountRole.builder()
                    .account(account)
                    .role(roleAdmin)
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
