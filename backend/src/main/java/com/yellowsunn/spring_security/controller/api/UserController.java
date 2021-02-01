package com.yellowsunn.spring_security.controller.api;

import com.yellowsunn.spring_security.domain.dto.UserDto;
import com.yellowsunn.spring_security.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;

/**
 * 사용자를 관리하는 Controller (권한 - ROLE_ADMIN)
 */
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // 회원가입에는 인증 권한이 필요없다.
    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public void register(@RequestBody UserDto userDto) {
        userService.register(userDto);
    }

    // 사용자 정보 업데이트
    @PutMapping("/admin/update")
    public void update(@RequestBody UserDto userDto) {
        userService.update(userDto);
    }

    // 특정 사용자 삭제
    @DeleteMapping("/admin/delete")
    public void delete(@RequestBody UserDto userDto) {
        userService.delete(userDto.getUsername());
    }

    // 사용자 정보를 불러온다
    @GetMapping("/admin/list")
    public Page<UserDto> list(@Nullable @RequestParam String search, @PageableDefault(size = 10) Pageable pageable) {
        return userService.findUsersBySearchCondition(search, pageable);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public String error(Exception e) {
        return e.getMessage();
    }
}
