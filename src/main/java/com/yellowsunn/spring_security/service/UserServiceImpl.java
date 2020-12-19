package com.yellowsunn.spring_security.service;

import com.yellowsunn.spring_security.domain.dto.UserDto;
import com.yellowsunn.spring_security.domain.entity.Account;
import com.yellowsunn.spring_security.domain.entity.AccountRole;
import com.yellowsunn.spring_security.domain.entity.Role;
import com.yellowsunn.spring_security.repository.AccountRepository;
import com.yellowsunn.spring_security.repository.AccountRoleRepository;
import com.yellowsunn.spring_security.repository.RoleHierarchyRepository;
import com.yellowsunn.spring_security.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {

    private final AccountRepository accountRepository;
    private final AccountRoleRepository accountRoleRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public void register(UserDto userDto) {
        encryptUser(userDto);
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

    private void encryptUser(UserDto userDto) {
        userDto.setUsername(userDto.getUsername().toLowerCase());
        userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));
    }

    private void checkUsername(String username) {
        Optional<Account> accountOptional = accountRepository.findByUsername(username);
        if (accountOptional.isPresent()) {
            throw new IllegalArgumentException("The name already exists.");
        }
    }
}
