package com.october.back.security.oauth2;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException, ServletException {
        // 소셜 로그인 인증 실패 처리
        if (exception instanceof OAuth2AuthenticationException) {
            OAuth2AuthenticationException oAuth2Exception = (OAuth2AuthenticationException) exception;
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Social login failed: " + oAuth2Exception.getMessage());
        } else {
            // 기타 인증 실패 처리
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Authentication failed: " + exception.getMessage());
        }
    }
}
