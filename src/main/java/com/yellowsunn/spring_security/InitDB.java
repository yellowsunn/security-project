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
        private Role USER, MANAGER, ADMIN;

        public void dbInit() {
            createRoles();
            createUser("root", "1111", ADMIN);
            createUser("jick", "eXM1p385Z!C9", ADMIN);
            createUser("stoover", "Q9GU38Mq4od&", ADMIN);
            createUser("cavities", "PlegErogYrIE", MANAGER);
            createUser("pesky", "5*52OTH2g^Xc", MANAGER);
            createUser("cheery", "eXQN05h$&$T5", MANAGER);
            createUser("lemur", "fU7X4E2@R1Tr", MANAGER);
            createUser("machete", "$ijHU$5h9#2b", MANAGER);
            createUser("jibboom", "WA2mA#5e818f", USER);
            createUser("spoker", "gPh#c9@Kq#Y2", USER);
            createUser("capricorn", "454xjeHEYR^v", USER);
            createUser("snubble", "2*Kt$H^Y6i4l", USER);
            createUser("oniony", "cIMIGAP92^R^", USER);
            createUser("anduin", "a3kuVx4!8TYA", USER);
            createUser("yoody", "#@F$81&4XKIc", USER);
            createUser("decor", "@5l01HEYnvZq", USER);
            createUser("bayton", "W1y#iD7HOZ8l", USER);
            createUser("maple", "H$%0uw5mCoR0", USER);
            createUser("poodle", "rj^7f9VO!4SI", USER);
            createUser("whizz", "WyunMH3zGb!3", USER);
            createUser("zone", "2e3&7oiV^vft", USER);
            createUser("orange", "^*54Ej2PKgep", USER);
            createUser("cranky", "rOarNiCirtIV", USER);
            createUser("swag", "BeGrADrAmeNT", USER);
            createUser("isaac", "MTZw4#xe2@YV", USER);
            createUser("waterbrook", "^3QB5eh#MU^n", USER);
            createUser("tirith", "@X6&5svaMG7q", USER);
            createUser("pungent", "H9ecjJFf53^B", USER);
            createUser("egbert", "@22P@kyt4hD5", USER);
            createUser("runner", "7Jk6FN!@j2xv", USER);
        }

        private void createRoles() {
            USER = Role.builder()
                    .name("ROLE_USER")
                    .build();

            MANAGER = Role.builder()
                    .name("ROLE_MANAGER")
                    .build();

            ADMIN = Role.builder()
                    .name("ROLE_ADMIN")
                    .build();

            // 계층구조 설정
            RoleHierarchy userHierarchy = RoleHierarchy.builder()
                    .role(USER)
                    .build();

            RoleHierarchy managerHierarchy = RoleHierarchy.builder()
                    .role(MANAGER)
                    .parent(userHierarchy)
                    .build();

            RoleHierarchy adminHierarchy = RoleHierarchy.builder()
                    .role(ADMIN)
                    .parent(managerHierarchy)
                    .build();

            em.persist(adminHierarchy);
        }
        private void createUser(String username, String password, Role role) {
            Account account = Account.builder()
                    .username(username)
                    .password(passwordEncoder.encode(password))
                    .build();

            em.persist(AccountRole.builder()
                    .account(account)
                    .role(role)
                    .build());
        }
    }

}
