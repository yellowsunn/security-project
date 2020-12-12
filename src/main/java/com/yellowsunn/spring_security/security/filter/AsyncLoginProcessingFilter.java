package com.yellowsunn.spring_security.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yellowsunn.spring_security.domain.dto.UserDto;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.StringUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AsyncLoginProcessingFilter extends AbstractAuthenticationProcessingFilter {

    private final ObjectMapper objectMapper = new ObjectMapper();

    public AsyncLoginProcessingFilter() {
        super(new AntPathRequestMatcher("/api/login"));
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        UserDto userDto = objectMapper.readValue(request.getReader(), UserDto.class);
        if (!StringUtils.hasLength(userDto.getUsername()) || !StringUtils.hasLength(userDto.getPassword())) {
            throw new IllegalArgumentException("Username or Password is empty");
        }

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDto.getUsername(), userDto.getPassword());
        return this.getAuthenticationManager().authenticate(authenticationToken);
    }
}
