package com.yellowsunn.spring_security;

import com.yellowsunn.spring_security.domain.entity.Account;
import com.yellowsunn.spring_security.domain.entity.AccountRole;
import com.yellowsunn.spring_security.domain.entity.Role;
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
                    .username("user")
                    .password(passwordEncoder.encode("1111"))
                    .build();

            Role role = Role.builder()
                    .name("ROLE_USER")
                    .build();

            em.persist(AccountRole.builder()
                    .account(account)
                    .role(role)
                    .build());
        }
    }
}
