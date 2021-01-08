package com.yellowsunn.spring_security.aop;

import com.yellowsunn.spring_security.service.SecurityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class CheckAuthorityAop {

    private final SecurityService securityService;

    @Around("execution(* com.yellowsunn.spring_security.controller.api.*.*(..))" +
            "&& !execution(* com.yellowsunn.spring_security.controller.api.UserController.error(..))" +
            "&& !execution(* com.yellowsunn.spring_security.controller.api.UserController.register(..))" +
            "&& !execution(* com.yellowsunn.spring_security.controller.api.BoardController.showImage(..))")
    public Object execute(ProceedingJoinPoint joinPoint) throws Throwable {
        log.info("AOP START: " + joinPoint.toString());

        // DB의 권한과 인증 객체의 권한이 일치하지 않는 경우 동기화
        securityService.syncAuthority();

        try {
            return joinPoint.proceed();
        } finally {
            log.info("AOP END: " + joinPoint.toString());
        }
    }
}
