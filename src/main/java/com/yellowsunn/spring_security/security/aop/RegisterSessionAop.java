package com.yellowsunn.spring_security.security.aop;

import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;

@Aspect
@Component
@RequiredArgsConstructor
public class RegisterSessionAop {

    private final SessionRegistry sessionRegistry;

    @Around("execution(* com.yellowsunn.spring_security.security.handler.CustomAuthenticationSuccessHandler.onAuthenticationSuccess(..))")
    public Object execute(ProceedingJoinPoint joinPoint) throws Throwable {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String sessionId = RequestContextHolder.currentRequestAttributes().getSessionId();
        try {
            return joinPoint.proceed();
        } finally {
            sessionRegistry.registerNewSession(sessionId, authentication.getPrincipal());
        }
    }
}
