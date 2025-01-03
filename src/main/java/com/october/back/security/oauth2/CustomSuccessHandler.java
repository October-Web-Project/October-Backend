package com.october.back.security.oauth2;

import com.october.back.security.oauth2.service.CustomOAuth2User;
import com.october.back.util.jwt.JwtUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

@Component
public class CustomSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtUtil jwtUtil;

    public CustomSuccessHandler(JwtUtil jwtUtil) {

        this.jwtUtil = jwtUtil;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        CustomOAuth2User customUserDetails = (CustomOAuth2User) authentication.getPrincipal();

        String username = customUserDetails.getName();

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority auth = iterator.next();
        String role = auth.getAuthority();

        String token = jwtUtil.createJwt(username, role, 60*60*1000L);
        response.addCookie(createCookie("Authorization", token));
        // OAuth2 제공자 확인
        String registrationId = customUserDetails.getUserDto().getName().split(" ")[0];

        // 동적으로 리다이렉트 URL 설정
        // todo: 추후 프론트 배포 주소에 맞게 수정해야됨
        String redirectUrl;
        if ("kakao".equalsIgnoreCase(registrationId)) {
            redirectUrl = "http://localhost:3000/api/auth/callback/kakao";
        } else if ("naver".equalsIgnoreCase(registrationId)) {
            redirectUrl = "http://localhost:3000/api/auth/callback/naver";
        } else {
            redirectUrl = "/api/main"; // 기본 리다이렉트 URL
        }
        response.sendRedirect(redirectUrl);
    }

    private Cookie createCookie(String key, String value) {

        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(60*60*60);
        //cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setHttpOnly(true);

        return cookie;
    }
}
