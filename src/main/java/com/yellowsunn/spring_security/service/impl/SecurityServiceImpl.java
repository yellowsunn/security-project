package com.yellowsunn.spring_security.service.impl;

import com.yellowsunn.spring_security.domain.dto.UserDto;
import com.yellowsunn.spring_security.domain.entity.Account;
import com.yellowsunn.spring_security.domain.entity.AccountRole;
import com.yellowsunn.spring_security.domain.entity.Role;
import com.yellowsunn.spring_security.domain.entity.RoleHierarchy;
import com.yellowsunn.spring_security.repository.AccountRepository;
import com.yellowsunn.spring_security.repository.AccountRoleRepository;
import com.yellowsunn.spring_security.repository.RoleHierarchyRepository;
import com.yellowsunn.spring_security.security.CustomUserDetails;
import com.yellowsunn.spring_security.service.SecurityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.RememberMeAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SecurityServiceImpl implements SecurityService {

    private final RoleHierarchyRepository roleHierarchyRepository;
    private final AccountRepository accountRepository;
    private final AccountRoleRepository accountRoleRepository;

    @Override
    public UserDto currentUserInfo() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        UserDto.UserDtoBuilder userDtoBuilder = UserDto.builder();
        setUsername(auth, userDtoBuilder);
        setAuthority(auth, userDtoBuilder);

        return userDtoBuilder.build();
    }

    @Override
    public void syncAuthority() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth != null) {
            // RememberMe 토큰일경우 UsernamePasswordAuthenticationToken 로 변경
            if (auth instanceof RememberMeAuthenticationToken) {
                Authentication newAuth = new UsernamePasswordAuthenticationToken(auth.getPrincipal(), null, auth.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(newAuth);
            }

            CustomUserDetails userDetails = (CustomUserDetails) auth.getPrincipal();
            Optional<Account> accountOptional = accountRepository.findByUsername(userDetails.getUsername());
            if (accountOptional.isEmpty()) {
                throw new IllegalStateException("Invalid account");
            }
            Optional<AccountRole> accountRoleOptional = accountRoleRepository.findByAccount(accountOptional.get());
            if (accountRoleOptional.isEmpty()) {
                throw new IllegalArgumentException("Invalid account or role");
            }

            Role role = accountRoleOptional.get().getRole();
            Collection<? extends GrantedAuthority> authAuthorities = auth.getAuthorities();

            List<GrantedAuthority> dbAuthorities = getAuthorities(role);

            // 권한이 DB와 일치하지 않는경우 동기화 작업
            if (authAuthorities.size() != dbAuthorities.size()) {
                log.warn("권한이 다르다!");
                Authentication newAuth = new UsernamePasswordAuthenticationToken(auth.getPrincipal(), null, dbAuthorities);
                SecurityContextHolder.clearContext();
                SecurityContextHolder.getContext().setAuthentication(newAuth);
            }
        }
    }

    private void setUsername(Authentication auth, UserDto.UserDtoBuilder userDtoBuilder) {
        CustomUserDetails customUserDetails = (CustomUserDetails) auth.getPrincipal();
        userDtoBuilder.username(customUserDetails.getUsername());
    }

    private void setAuthority(Authentication auth, UserDto.UserDtoBuilder userDtoBuilder) {
        Set<String> authorities = auth.getAuthorities()
                .stream().map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());

        if (authorities.contains("ROLE_ADMIN")) {
            userDtoBuilder.role("ADMIN");
        } else if (authorities.contains("ROLE_MANAGER")) {
            userDtoBuilder.role("MANAGER");
        } else if (authorities.contains("ROLE_USER")) {
            userDtoBuilder.role("USER");
        }
    }

    private List<GrantedAuthority> getAuthorities(Role role) {
        Optional<RoleHierarchy> roleHierarchyOptional = roleHierarchyRepository.findByRole(role);
        if (roleHierarchyOptional.isEmpty()) {
            throw new IllegalArgumentException("Invalid role hierarchy");
        }

        List<Role> roles = new ArrayList<>();
        roles.add(roleHierarchyOptional.get().getRole());

        RoleHierarchy parent = roleHierarchyOptional.get().getParent();
        while (parent != null) {
            roles.add(parent.getRole());
            parent = parent.getParent();
        }

        List<GrantedAuthority> authorities = new ArrayList<>();
        for (Role r : roles) {
            authorities.add(new SimpleGrantedAuthority(r.getName()));
        }

        return authorities;
    }
}
