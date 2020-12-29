package com.yellowsunn.spring_security.service;

import com.yellowsunn.spring_security.domain.dto.UserDto;
import org.springframework.security.access.annotation.Secured;

public interface SecurityService {

    // SecurityContext 객체에 등록된 인증 객체로부터 사용자 정보를 가져온다
    UserDto currentUserInfo();

    // SecurityContext 객체가 변경될 필요가 있으면 변경
    @Secured("ROLE_ADMIN")
    void changeSecurityContext(UserDto userDto);
}
