package com.yellowsunn.spring_security.domain.dto;

import lombok.Data;

import java.util.List;

@Data
public class UserDto {

    private String username;
    private String password;
    private boolean rememberMe;
    private List<String> roles;
}
