package com.yellowsunn.spring_security.domain.dto;

import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 사용자 목록 DTO
 */

@Data
@Builder
public class UsersDto {

    private List<UserDto> users = new ArrayList<>();
    private int size;
}
