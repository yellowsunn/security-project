package com.yellowsunn.spring_security.security.config;

import com.yellowsunn.spring_security.security.common.CustomRememberMeServices;
import com.yellowsunn.spring_security.security.filter.CustomAuthenticationProcessingFilter;
import com.yellowsunn.spring_security.security.handler.CustomAuthenticationFailureHandler;
import com.yellowsunn.spring_security.security.handler.CustomAuthenticationSuccessHandler;
import com.yellowsunn.spring_security.security.provider.CustomAuthenticationProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.rememberme.AbstractRememberMeServices;
import org.springframework.security.web.authentication.session.ConcurrentSessionControlAuthenticationStrategy;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    public static final String REMEMBER_ME_KEY = "yellowsunn_key";

    private final UserDetailsService userDetailsService;
    private final AuthenticationEntryPoint authenticationEntryPoint;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                // 인가 API
                .antMatcher("/api/**")
                .authorizeRequests()
                .antMatchers("/api/register").permitAll()
                .antMatchers("/api/user").hasRole("USER")
                .antMatchers("/api/manager").hasRole("MANAGER")
                .antMatchers("/api/admin/**").hasRole("ADMIN")
                .anyRequest().authenticated()

                // 로그아웃 처리
                .and().logout()
                .logoutUrl("/api/logout")
                .logoutSuccessHandler((request, response, authentication) -> {})
                .deleteCookies("JSESSIONID", "remember-me")

                // 예외 처리
                .and().exceptionHandling()
                .authenticationEntryPoint(authenticationEntryPoint) // 인증 실패시 처리

                // Remember Me 인증
                .and().rememberMe()
                .key(REMEMBER_ME_KEY)
                .rememberMeServices(rememberMeServices())

                // csrf
//                .and().csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())

                // 동시 세션 제어 (이전 사용자 세션 만료)
                .and().sessionManagement()
                .maximumSessions(1)
                .sessionRegistry(sessionRegistry());

        http.csrf().disable();
        // Custom 인증 필터가 우선 동작
        http.addFilterBefore(authenticationProcessingFilter(), UsernamePasswordAuthenticationFilter.class);

        http.cors();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.addAllowedOriginPattern("*");
//        configuration.addAllowedOrigin("http://localhost:8081");
        configuration.addAllowedHeader("*");
        configuration.addAllowedMethod("*");
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public AbstractAuthenticationProcessingFilter authenticationProcessingFilter() throws Exception {
        CustomAuthenticationProcessingFilter authenticationProcessingFilter = new CustomAuthenticationProcessingFilter();
        authenticationProcessingFilter.setAuthenticationManager(super.authenticationManagerBean());
        authenticationProcessingFilter.setAuthenticationSuccessHandler(authenticationSuccessHandler());
        authenticationProcessingFilter.setAuthenticationFailureHandler(authenticationFailureHandler());
        authenticationProcessingFilter.setRememberMeServices(rememberMeServices());
        authenticationProcessingFilter.setSessionAuthenticationStrategy(sessionStrategy());
        return authenticationProcessingFilter;
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        return new CustomAuthenticationProvider(userDetailsService, passwordEncoder());
    }

    @Bean
    public AuthenticationSuccessHandler authenticationSuccessHandler() {
        return new CustomAuthenticationSuccessHandler();
    }

    @Bean
    public AuthenticationFailureHandler authenticationFailureHandler() {
        return new CustomAuthenticationFailureHandler();
    }

    @Bean
    public AbstractRememberMeServices rememberMeServices() {
        CustomRememberMeServices rememberMeServices = new CustomRememberMeServices(REMEMBER_ME_KEY, userDetailsService);
        rememberMeServices.setAlwaysRemember(false);
        rememberMeServices.setParameter("rememberMe");
        return rememberMeServices;
    }

    @Bean
    public SessionRegistry sessionRegistry() {
        return new SessionRegistryImpl();
    }

    @Bean
    public SessionAuthenticationStrategy sessionStrategy() {
        return new ConcurrentSessionControlAuthenticationStrategy(sessionRegistry());
    }
}
