package com.yellowsunn.spring_security.security.service;

import com.yellowsunn.spring_security.domain.entity.Account;
import com.yellowsunn.spring_security.domain.entity.AccountRole;
import com.yellowsunn.spring_security.domain.entity.Role;
import com.yellowsunn.spring_security.domain.entity.RoleHierarchy;
import com.yellowsunn.spring_security.repository.AccountRepository;
import com.yellowsunn.spring_security.repository.AccountRoleRepository;
import com.yellowsunn.spring_security.repository.RoleHierarchyRepository;
import com.yellowsunn.spring_security.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class CustomUserDetailsService implements UserDetailsService {

    private final AccountRepository accountRepository;
    private final AccountRoleRepository accountRoleRepository;
    private final RoleHierarchyRepository roleHierarchyRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Account> accountOptional = accountRepository.findByUsername(username);
        if (accountOptional.isEmpty()) {
            throw new UsernameNotFoundException("UsernameNotFoundException");
        }
        Account account = accountOptional.get();

        // 권한 부여
        List<GrantedAuthority> authorities = getAuthorities(account);

        return new CustomUserDetails(account, authorities);
    }

    private List<GrantedAuthority> getAuthorities(Account account) {
        AccountRole accountRole = accountRoleRepository.findByAccount(account).get();
        RoleHierarchy roleHierarchy = roleHierarchyRepository.findByRole(accountRole.getRole()).get();

        List<Role> roles = new ArrayList<>();
        roles.add(roleHierarchy.getRole());

        RoleHierarchy parent = roleHierarchy.getParent();
        while (parent != null) {
            roles.add(parent.getRole());
            parent = parent.getParent();
        }

        List<GrantedAuthority> authorities = new ArrayList<>();
        for (Role role : roles) {
            authorities.add(new SimpleGrantedAuthority(role.getName()));
        }

        return authorities;
    }
}

