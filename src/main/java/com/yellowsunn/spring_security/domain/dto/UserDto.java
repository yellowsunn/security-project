package com.yellowsunn.spring_security.domain.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

/**
 * 사용자의 정보를 담은 DTO
 */

@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Getter @Setter
@Builder
@ToString
public class UserDto {

    private String username;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String password;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String role;
}

