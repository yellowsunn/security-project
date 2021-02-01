package com.yellowsunn.spring_security.service;

import com.yellowsunn.spring_security.domain.dto.UserDto;

public interface SecurityService {

    // SecurityContext 객체에 등록된 인증 객체로부터 사용자 정보를 가져온다
    UserDto currentUserInfo();

    // DB의 권한과 인증 객체의 권한이 일치하지 않는 경우 동기화
    void syncAuthority();
}
