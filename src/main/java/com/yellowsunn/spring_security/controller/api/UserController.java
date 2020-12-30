package com.yellowsunn.spring_security.controller.api;

import com.yellowsunn.spring_security.domain.dto.UserDto;
import com.yellowsunn.spring_security.service.SecurityService;
import com.yellowsunn.spring_security.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public void register(@RequestBody UserDto userDto) {
        userService.register(userDto);
    }

    @PutMapping("/admin/update")
    public void update(@RequestBody UserDto userDto) {
        userService.update(userDto);
    }

    @DeleteMapping("/admin/delete")
    public void delete(@RequestBody UserDto userDto) {
        userService.delete(userDto.getUsername());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public String error(Exception e) {
        return e.getMessage();
    }
}
