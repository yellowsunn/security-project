package com.yellowsunn.spring_security.security.config;

import com.yellowsunn.spring_security.security.filter.AsyncLoginProcessingFilter;
import com.yellowsunn.spring_security.security.handler.AsyncAuthenticationFailureHandler;
import com.yellowsunn.spring_security.security.handler.AsyncAuthenticationSuccessHandler;
import com.yellowsunn.spring_security.security.provider.AsyncAuthenticationProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserDetailsService userDetailsService;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .anyRequest().authenticated();

        http.cors()
                .configurationSource(request -> new CorsConfiguration().applyPermitDefaultValues());

        http.addFilterBefore(authenticationProcessingFilter(), UsernamePasswordAuthenticationFilter.class)
                .csrf().disable();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        return new AsyncAuthenticationProvider(userDetailsService, passwordEncoder());
    }

    @Bean
    public AuthenticationSuccessHandler authenticationSuccessHandler() {
        return new AsyncAuthenticationSuccessHandler();
    }

    @Bean
    public AuthenticationFailureHandler authenticationFailureHandler() {
        return new AsyncAuthenticationFailureHandler();
    }

    @Bean
    public AbstractAuthenticationProcessingFilter authenticationProcessingFilter() throws Exception {
        AsyncLoginProcessingFilter asyncLoginProcessingFilter = new AsyncLoginProcessingFilter();
        asyncLoginProcessingFilter.setAuthenticationManager(super.authenticationManagerBean());
        asyncLoginProcessingFilter.setAuthenticationSuccessHandler(authenticationSuccessHandler());
        asyncLoginProcessingFilter.setAuthenticationFailureHandler(authenticationFailureHandler());
        return asyncLoginProcessingFilter;
    }
}
