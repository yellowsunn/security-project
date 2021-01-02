package com.yellowsunn.spring_security.service;

import com.yellowsunn.spring_security.domain.dto.UserDto;
import com.yellowsunn.spring_security.domain.dto.UsersDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.annotation.Secured;

import java.util.Optional;

public interface UserService {

    void register(UserDto userDto);

    @Secured("ROLE_ADMIN")
    void update(UserDto userDto);

    void delete(String username);

    Optional<UserDto> findByUsername(String username);

    UsersDto findAll();

    Page<UserDto> findUsersBySearchCondition(String search, Pageable pageable);
}
