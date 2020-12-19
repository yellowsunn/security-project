package com.yellowsunn.spring_security.service;

import com.yellowsunn.spring_security.domain.dto.UserDto;
import com.yellowsunn.spring_security.domain.entity.Account;
import com.yellowsunn.spring_security.domain.entity.AccountRole;
import com.yellowsunn.spring_security.domain.entity.Role;
import com.yellowsunn.spring_security.repository.AccountRepository;
import com.yellowsunn.spring_security.repository.AccountRoleRepository;
import com.yellowsunn.spring_security.repository.RoleRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class UserServiceTest {
    @Autowired AccountRepository accountRepository;
    @Autowired RoleRepository roleRepository;
    @Autowired AccountRoleRepository accountRoleRepository;

    @Test
    void register() {
        UserDto userDto = new UserDto();
        userDto.setUsername("admin");
        userDto.setPassword("1111");
        userDto.setRole("ROLE_USER");

        checkUsername(userDto.getUsername());
        Account account = accountRepository.save(Account.builder()
                .username(userDto.getUsername())
                .password(userDto.getPassword())
                .build());

        Optional<Role> roleUserOptional = roleRepository.findByName("ROLE_USER");
        if (roleUserOptional.isEmpty()) {
            throw new IllegalArgumentException("Invalid role");
        }

        accountRoleRepository.save(AccountRole.builder()
                .account(account)
                .role(roleUserOptional.get())
                .build());
    }

    private void checkUsername(String username) {
        Optional<Account> accountOptional = accountRepository.findByUsername(username);
        if (accountOptional.isPresent()) {
            throw new IllegalArgumentException("The name already exists.");
        }
    }
}