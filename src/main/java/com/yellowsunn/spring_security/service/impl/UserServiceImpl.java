package com.yellowsunn.spring_security.service.impl;

import com.yellowsunn.spring_security.domain.dto.UserDto;
import com.yellowsunn.spring_security.domain.dto.UsersDto;
import com.yellowsunn.spring_security.domain.entity.Account;
import com.yellowsunn.spring_security.domain.entity.AccountRole;
import com.yellowsunn.spring_security.domain.entity.Role;
import com.yellowsunn.spring_security.repository.AccountRepository;
import com.yellowsunn.spring_security.repository.AccountRoleRepository;
import com.yellowsunn.spring_security.repository.RoleRepository;
import com.yellowsunn.spring_security.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {

    private final AccountRepository accountRepository;
    private final AccountRoleRepository accountRoleRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public void register(UserDto userDto) {
        userDto.setUsername(userDto.getUsername().toLowerCase());
        userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));
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

    @Override
    public void update(UserDto userDto) {
        Optional<Account> accountOptional = accountRepository.findByUsername(userDto.getUsername());
        if (accountOptional.isEmpty()) {
            throw new IllegalArgumentException("Invalid account");
        }
        Account account = accountOptional.get();

        updatePassword(userDto, account);
        updateRole(userDto, account);
    }

    @Override
    public UsersDto findAll() {
        List<AccountRole> accountRoles = accountRoleRepository.findCustomAll();
        List<UserDto> users = accountRoles.stream()
                .map(accountRole -> UserDto.builder()
                        .username(accountRole.getAccount().getUsername())
                        .password(accountRole.getAccount().getPassword())
                        .role(accountRole.getRole().getName())
                        .build()
                ).collect(Collectors.toList());

        return UsersDto.builder()
                .users(users)
                .size(users.size())
                .build();
    }

    private void checkUsername(String username) {
        Optional<Account> accountOptional = accountRepository.findByUsername(username);
        if (accountOptional.isPresent()) {
            throw new IllegalArgumentException("Account already exists");
        }
    }

    private void updatePassword(UserDto userDto, Account account) {
        if (StringUtils.hasText(userDto.getPassword())) {
            if (!passwordEncoder.matches(userDto.getPassword(), account.getPassword())) {
                account.changePassword(passwordEncoder.encode(userDto.getPassword()));
            }
        }
    }

    private void updateRole(UserDto userDto, Account account) {
        Optional<Role> roleOptional = roleRepository.findByName(userDto.getRole());
        if (roleOptional.isEmpty()) {
            throw new IllegalArgumentException("Invalid role");
        }

        Optional<AccountRole> accountRoleOptional = accountRoleRepository.findByAccount(account);
        if (accountRoleOptional.isEmpty()) {
            throw new IllegalArgumentException("Invalid account or role");
        }

        accountRoleOptional.get().changeRole(roleOptional.get());
    }
}
