package com.yellowsunn.spring_security.controller.api;

import com.yellowsunn.spring_security.domain.dto.UserDto;
import com.yellowsunn.spring_security.service.SecurityService;
import com.yellowsunn.spring_security.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class SecurityController {

    private final UserService userService;
    private final SecurityService securityService;

    // 로그인한 경우 사용자 정보를 반환
    @GetMapping("/home")
    public UserDto userInfo() {
        return securityService.currentUserInfo();
    }

    @GetMapping("/user")
    public ResponseEntity<?> user() {
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/manager")
    public ResponseEntity<?> manager() {
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/admin")
    public ResponseEntity<?> admin() {
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
