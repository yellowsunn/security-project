package com.yellowsunn.spring_security.service;

import com.yellowsunn.spring_security.domain.dto.UserDto;

public interface UserService {
    void register(UserDto userDto);
}
