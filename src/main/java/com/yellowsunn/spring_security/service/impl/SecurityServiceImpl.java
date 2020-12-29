package com.yellowsunn.spring_security.service.impl;

import com.yellowsunn.spring_security.domain.dto.UserDto;
import com.yellowsunn.spring_security.domain.entity.Role;
import com.yellowsunn.spring_security.domain.entity.RoleHierarchy;
import com.yellowsunn.spring_security.repository.RoleHierarchyRepository;
import com.yellowsunn.spring_security.repository.RoleRepository;
import com.yellowsunn.spring_security.security.CustomUserDetails;
import com.yellowsunn.spring_security.service.SecurityService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SecurityServiceImpl implements SecurityService {

    private final RoleRepository roleRepository;
    private final RoleHierarchyRepository roleHierarchyRepository;

    @Override
    public UserDto currentUserInfo() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        UserDto.UserDtoBuilder userDtoBuilder = UserDto.builder();
        setUsername(auth, userDtoBuilder);
        setAuthority(auth, userDtoBuilder);

        return userDtoBuilder.build();
    }

    @Override
    public void changeSecurityContext(UserDto userDto) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        // 현재 로그인된 사용자일 경우에 변경
        if (auth != null && isSameUser(userDto, auth)) {
            List<GrantedAuthority> authorities = getAuthorities(userDto);

            Authentication newAuth = new UsernamePasswordAuthenticationToken(auth.getPrincipal(), null, authorities);
            SecurityContextHolder.clearContext();
            SecurityContextHolder.getContext().setAuthentication(newAuth);
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

    private boolean isSameUser(UserDto userDto, Authentication auth) {
        return ((CustomUserDetails) auth.getPrincipal()).getUsername().equals(userDto.getUsername());
    }

    private List<GrantedAuthority> getAuthorities(UserDto userDto) {
        Optional<Role> roleOptional = roleRepository.findByName(userDto.getRole());
        if (roleOptional.isEmpty()) {
            throw new IllegalArgumentException("Invalid role");
        }

        Optional<RoleHierarchy> roleHierarchyOptional = roleHierarchyRepository.findByRole(roleOptional.get());
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
        for (Role role : roles) {
            authorities.add(new SimpleGrantedAuthority(role.getName()));
        }

        return authorities;
    }
}
