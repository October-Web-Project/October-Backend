package com.october.back.util.jwt;

import com.october.back.global.common.ErrorCode;
import com.october.back.global.exception.JwtException;
import com.october.back.security.oauth2.service.CustomOAuth2User;
import com.october.back.user.entity.UserRole;
import com.october.back.user.entity.dto.UserDto;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class JwtFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;

    public JwtFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String authorization = null;
        Cookie[] cookies = request.getCookies();
        String requestUri = request.getRequestURI();

        // 인증이 필요 없는 경로 리스트
        if (requestUri.matches("^\\/login(?:\\/.*)?$") ||
                requestUri.matches("^\\/oauth2(?:\\/.*)?$") ||
                requestUri.matches("^\\/favicon.ico$") ||
                requestUri.matches("^\\/firebase(?:\\/.*)?$") || // 이 부분에서 firebase 아래 모든 경로가 포함됩니다.
                requestUri.matches("^\\/firebase-messaging-sw.js$") || // 명시적으로 추가 (만약 독립적인 파일로 처리하고자 할 경우)
                requestUri.matches("^\\/ws(?:\\/.*)?$") ||
                requestUri.matches("^\\/js(?:\\/.*)?$") ||
                requestUri.matches("^\\/swagger-ui(?:\\/.*)?$") || // Swagger UI
                requestUri.matches("^\\/v3\\/api-docs(?:\\/.*)?$") ||
                requestUri.matches("^\\/api\\/test(?:\\/.*)?$")) { // Swagger API Docs

            filterChain.doFilter(request, response);
            return;
        }

        // Authorization 쿠키 추출
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("Authorization")) {
                    authorization = cookie.getValue();
                }
            }
        }

        // Authorization 헤더가 없으면 예외 처리
        if (authorization == null) {
            throw new JwtException("토큰이 비어 있습니다.", ErrorCode.EMPTY_TOKEN);
        }

        String token = authorization;

        // 토큰 만료 검증
        if (jwtUtil.isExpired(token)) {
            throw new JwtException("토큰이 만료되었습니다.", ErrorCode.EXPIRED_TOKEN);
        }

        // 토큰에서 username과 role 획득
        String username = jwtUtil.getUsername(token);
        String role = jwtUtil.getRole(token);

        // userDTO를 생성하여 값 set
        UserDto user = UserDto.builder()
                .name(username)
                .role(UserRole.valueOf(role))
                .build();

        // UserDetails에 회원 정보 객체 담기
        CustomOAuth2User customOAuth2User = new CustomOAuth2User(user);

        // 스프링 시큐리티 인증 토큰 생성
        Authentication authToken = new UsernamePasswordAuthenticationToken(customOAuth2User, null,
                customOAuth2User.getAuthorities());

        // 세션에 사용자 등록
        SecurityContextHolder.getContext().setAuthentication(authToken);

        filterChain.doFilter(request, response);
    }
}
