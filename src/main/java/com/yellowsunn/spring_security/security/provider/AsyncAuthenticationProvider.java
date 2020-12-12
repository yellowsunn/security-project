package com.yellowsunn.spring_security.security.provider;

import com.yellowsunn.spring_security.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

@RequiredArgsConstructor
public class AsyncAuthenticationProvider implements AuthenticationProvider {

    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = (String) authentication.getCredentials();

        CustomUserDetails userDetails = (CustomUserDetails) userDetailsService.loadUserByUsername(username);

        // 패스워드 검증
        if (!passwordEncoder.matches(password, userDetails.getAccount().getPassword())) {
            throw new BadCredentialsException("BadCredentialsException");
        }

        return new UsernamePasswordAuthenticationToken(userDetails.getAccount(), null, userDetails.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
