package com.yellowsunn.spring_security.domain.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class WebsocketDto extends UserDto {
    private boolean isChanged;
    private boolean isDeleted;

    public void setUser(UserDto userDto) {
        super.setUsername(userDto.getUsername());
        super.setPassword(userDto.getPassword());
        super.setRole(userDto.getRole());
    }
}
