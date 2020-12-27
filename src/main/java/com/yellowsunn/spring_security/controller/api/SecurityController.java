package com.yellowsunn.spring_security.controller.api;

import com.yellowsunn.spring_security.domain.dto.UserDto;
import com.yellowsunn.spring_security.domain.dto.UsersDto;
import com.yellowsunn.spring_security.domain.entity.Account;
import com.yellowsunn.spring_security.security.CustomUserDetails;
import com.yellowsunn.spring_security.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.RememberMeAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class SecurityController {

    private final SessionRegistry sessionRegistry;
    private final UserService userService;

    @GetMapping("")
    public UserDto main(HttpServletRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        // 세션 수동 등록
        sessionRegistry.registerNewSession(request.getSession().getId(), authentication.getPrincipal());

        UserDto.UserDtoBuilder userDtoBuilder = UserDto.builder();

        setUsername(authentication, userDtoBuilder);
        setAuthority(authentication, userDtoBuilder);

        return userDtoBuilder.build();
    }

    @GetMapping("/user")
    public String user() {
        return "user";
    }

    @GetMapping("/manager")
    public String manager() {
        return "manager";
    }

    @GetMapping("/admin")
    public UsersDto admin() {
        return userService.findAll();
    }

    private void setUsername(Authentication authentication, UserDto.UserDtoBuilder userDtoBuilder) {
        // 일반 인증인 경우
        if (authentication instanceof UsernamePasswordAuthenticationToken) {
            Account account = (Account) authentication.getPrincipal();
            userDtoBuilder.username(account.getUsername());

            // Remember Me 인증인 경우
        } else if (authentication instanceof RememberMeAuthenticationToken) {
            CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
            userDtoBuilder.username(customUserDetails.getUsername());
        }
    }

    private void setAuthority(Authentication authentication, UserDto.UserDtoBuilder userDtoBuilder) {
        Set<String> authorities = authentication.getAuthorities()
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
}
