package com.yellowsunn.spring_security.controller;

import com.yellowsunn.spring_security.domain.dto.UserDto;
import com.yellowsunn.spring_security.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TestController {

    private final UserService userService;

    @GetMapping("/test")
    public Page<UserDto> test(@Nullable @RequestBody UserDto userDto, Pageable pageable) {
        String username = userDto != null ? userDto.getUsername() : null;
        return userService.findUsersBySearchCondition(username, pageable);
    }
}
