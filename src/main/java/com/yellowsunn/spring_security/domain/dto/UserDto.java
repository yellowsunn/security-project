package com.yellowsunn.spring_security.domain.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Getter @Setter
@Builder
@ToString
public class UserDto {

    private String username;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String password;
    private String role;
}

