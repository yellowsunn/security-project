package com.yellowsunn.spring_security.security.service;

import com.yellowsunn.spring_security.domain.entity.Account;
import com.yellowsunn.spring_security.domain.entity.AccountRole;
import com.yellowsunn.spring_security.domain.entity.Role;
import com.yellowsunn.spring_security.repository.AccountRepository;
import com.yellowsunn.spring_security.repository.AccountRoleRepository;
import com.yellowsunn.spring_security.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final AccountRepository accountRepository;
    private final AccountRoleRepository accountRoleRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Account> accountOptional = accountRepository.findByUsername(username);
        if (accountOptional.isEmpty()) {
            throw new UsernameNotFoundException("UsernameNotFoundException");
        }
        Account account = accountOptional.get();

        // 권한
        List<Role> roles = accountRoleRepository.findByAccount(account)
                .stream().map(AccountRole::getRole)
                .collect(Collectors.toList());

        List<GrantedAuthority> authorities = new ArrayList<>();
        for (Role role : roles) {
            authorities.add(new SimpleGrantedAuthority(role.getName()));
        }

        return new CustomUserDetails(account, authorities);
    }
}
