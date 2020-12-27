package com.yellowsunn.spring_security.service;

import com.yellowsunn.spring_security.domain.dto.UserDto;
import com.yellowsunn.spring_security.domain.dto.UsersDto;

public interface UserService {
    void register(UserDto userDto);

    UsersDto findAll();
}
