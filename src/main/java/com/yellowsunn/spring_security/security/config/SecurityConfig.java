package com.yellowsunn.spring_security.security.config;

import com.yellowsunn.spring_security.security.filter.CustomLoginProcessingFilter;
import com.yellowsunn.spring_security.security.handler.CustomAuthenticationFailureHandler;
import com.yellowsunn.spring_security.security.handler.CustomAuthenticationSuccessHandler;
import com.yellowsunn.spring_security.security.provider.CustomAuthenticationProvider;
import com.yellowsunn.spring_security.security.service.CustomRememberMeServices;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.rememberme.AbstractRememberMeServices;
import org.springframework.security.web.authentication.session.ConcurrentSessionControlAuthenticationStrategy;
import org.springframework.security.web.authentication.session.SessionAuthenticationException;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private static final String REMEMBER_ME_KEY = "yellowsunn_key";

    private final UserDetailsService userDetailsService;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(customAuthenticationProvider());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .antMatcher("/api/**")
                .authorizeRequests()
                .antMatchers("/api/register").permitAll()
                .antMatchers("/api/user").hasRole("USER")
                .antMatchers("/api/manager").hasRole("MANAGER")
                .antMatchers("/api/admin").hasRole("ADMIN")
                .anyRequest().authenticated()

                .and().logout()
                .logoutUrl("/api/logout")
                .logoutSuccessHandler((request, response, authentication) -> {}) // 로그아웃 후 아무작업 안함
                .deleteCookies("JSESSIONID", "remember-me")

                .and().rememberMe()
                .key(REMEMBER_ME_KEY)
                .rememberMeServices(customRememberMeServices())

                .and().csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())

                .and().sessionManagement()
                .maximumSessions(1)
                .sessionRegistry(sessionRegistry());

        http.addFilterBefore(customLoginProcessingFilter(), UsernamePasswordAuthenticationFilter.class);

        http.cors();
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
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider customAuthenticationProvider() {
        return new CustomAuthenticationProvider(userDetailsService, passwordEncoder());
    }

    @Bean
    public AuthenticationSuccessHandler customAuthenticationSuccessHandler() {
        return new CustomAuthenticationSuccessHandler();
    }

    @Bean
    public AuthenticationFailureHandler customAuthenticationFailureHandler() {
        return new CustomAuthenticationFailureHandler();
    }

    @Bean
    public AbstractAuthenticationProcessingFilter customLoginProcessingFilter() throws Exception {
        CustomLoginProcessingFilter customLoginProcessingFilter = new CustomLoginProcessingFilter();
        customLoginProcessingFilter.setRememberMeServices(customRememberMeServices());
        customLoginProcessingFilter.setAuthenticationManager(super.authenticationManagerBean());
        customLoginProcessingFilter.setAuthenticationSuccessHandler(customAuthenticationSuccessHandler());
        customLoginProcessingFilter.setAuthenticationFailureHandler(customAuthenticationFailureHandler());
        customLoginProcessingFilter.setSessionAuthenticationStrategy(sessionStrategy());
        return customLoginProcessingFilter;
    }

    @Bean
    public AbstractRememberMeServices customRememberMeServices() {
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
    public ServletListenerRegistrationBean<HttpSessionEventPublisher> httpSessionEventPublisher() { //(5)
        return new ServletListenerRegistrationBean<HttpSessionEventPublisher>(new HttpSessionEventPublisher());
    }

    @Bean
    public SessionAuthenticationStrategy sessionStrategy() {
        return new ConcurrentSessionControlAuthenticationStrategy(sessionRegistry());
    }
}
