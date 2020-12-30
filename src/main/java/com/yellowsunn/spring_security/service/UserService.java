package com.yellowsunn.spring_security.service;

import com.yellowsunn.spring_security.domain.dto.UserDto;
import com.yellowsunn.spring_security.domain.dto.UsersDto;
import org.springframework.security.access.annotation.Secured;

public interface UserService {

    void register(UserDto userDto);

    @Secured("ROLE_ADMIN")
    void update(UserDto userDto);

    UsersDto findAll();
}
