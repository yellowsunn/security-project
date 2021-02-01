package com.yellowsunn.spring_security.security.common;

import com.yellowsunn.spring_security.security.CustomUserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.rememberme.TokenBasedRememberMeServices;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * TokenBasedRememberMeServices 에서 username, password 불러오는 부분만 수정
 */
public class CustomRememberMeServices extends TokenBasedRememberMeServices {

    public CustomRememberMeServices(String key, UserDetailsService userDetailsService) {
        super(key, userDetailsService);
    }

    @Override
    public void onLoginSuccess(HttpServletRequest request, HttpServletResponse response, Authentication successfulAuthentication) {
        // 수정한 부분
        String username = ((UserDetails) successfulAuthentication.getPrincipal()).getUsername();
        String password = ((CustomUserDetails) successfulAuthentication.getPrincipal()).getAccount().getPassword();

        // If unable to find a username and password, just abort as
        // TokenBasedRememberMeServices is
        // unable to construct a valid token in this case.
        if (!StringUtils.hasLength(username)) {
            this.logger.debug("Unable to retrieve username");
            return;
        }
        if (!StringUtils.hasLength(password)) {
            UserDetails user = getUserDetailsService().loadUserByUsername(username);
            password = user.getPassword();
            if (!StringUtils.hasLength(password)) {
                this.logger.debug("Unable to obtain password for user: " + username);
                return;
            }
        }
        int tokenLifetime = calculateLoginLifetime(request, successfulAuthentication);
        long expiryTime = System.currentTimeMillis();
        // SEC-949
        expiryTime += 1000L * ((tokenLifetime < 0) ? TWO_WEEKS_S : tokenLifetime);
        String signatureValue = makeTokenSignature(expiryTime, username, password);
        setCookie(new String[] { username, Long.toString(expiryTime), signatureValue }, tokenLifetime, request,
                response);
        if (this.logger.isDebugEnabled()) {
            this.logger.debug(
                    "Added remember-me cookie for user '" + username + "', expiry: '" + new Date(expiryTime) + "'");
        }
    }
}
