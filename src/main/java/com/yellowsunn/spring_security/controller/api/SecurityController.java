package com.yellowsunn.spring_security.controller.api;

import com.yellowsunn.spring_security.domain.dto.UserDto;
import com.yellowsunn.spring_security.domain.dto.UsersDto;
import com.yellowsunn.spring_security.security.CustomUserDetails;
import com.yellowsunn.spring_security.service.SecurityService;
import com.yellowsunn.spring_security.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class SecurityController {

    private final UserService userService;
    private final SecurityService securityService;

    @GetMapping("/home")
    public UserDto userInfo() {
        return securityService.currentUserInfo();
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


}
