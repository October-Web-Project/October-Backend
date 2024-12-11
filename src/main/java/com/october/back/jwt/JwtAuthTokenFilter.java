package com.october.back.jwt;

import com.october.back.error.JwtErrorCode;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Configuration
@Slf4j
@RequiredArgsConstructor
public class JwtAuthTokenFilter extends OncePerRequestFilter {
    private final JwtTokenProvider jwtTokenProvider;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String path = request.getRequestURI();

        // 요청 경로 로깅
        log.info("Processing request for path: {}", path);

        // 특정 경로는 필터링 제외
        if (path.contains("/swagger") || path.contains("/v3/api-docs")
                || path.startsWith("/auth") || path.startsWith("/error")
                || path.equals("/normal/users/auth/login")
                || path.equals("/normal/users/reissue")) {
            filterChain.doFilter(request, response);
            return;
        }

        // Access Token 추출
        String AT = jwtTokenProvider.resolveAT(request);
        log.info("Access Token extracted: {}", AT);

        String RT = jwtTokenProvider.resolveRT(request);

        try {
            // `/reissue-token` 경로는 Refresh Token으로만 처리
            if (path.equals("/normal/users/reissue")) {
                if (RT != null && jwtTokenProvider.validateToken(RT)) {
                    filterChain.doFilter(request, response);
                    return;
                } else {
                    log.warn("Invalid or missing Refresh Token for path: {}", path);
                    setResponse(response, JwtErrorCode.JWT_COMPLEX_ERROR);
                    return;
                }
            }

            // 일반적인 Access Token 검증
            if (AT != null && jwtTokenProvider.validateToken(AT)) {
                log.info("Access Token validation successful for token: {}", AT);
                this.setAuthentication(AT);

                // ROLE 검증
                String userRole = jwtTokenProvider.extractUserRoleFromToken(AT);
                log.info("Extracted User Role: {}", userRole);

                if (path.startsWith("/admin") && !userRole.equals("ROLE_ADMIN")) {
                    log.warn("Access denied: Admin role required for path {}", path);
                    setForbiddenResponse(response, "Admin role required");
                    return;
                }
                if (path.startsWith("/users") && !userRole.equals("ROLE_USER") && !userRole.equals("ROLE_ADMIN")) {
                    log.warn("Access denied: User or Admin role required for path {}", path);
                    setForbiddenResponse(response, "User or Admin role required");
                    return;
                }
            } else {
                log.warn("Missing or invalid Access Token for path: {}", path);
                setResponse(response, JwtErrorCode.JWT_COMPLEX_ERROR);
                return;
            }
        } catch (Exception e) {
            log.error("JWT validation failed: {}", e.getMessage());
            setResponse(response, JwtErrorCode.JWT_COMPLEX_ERROR);
            return;
        }

        // 다음 필터로 요청 전달
        filterChain.doFilter(request, response);
    }


    private void setAuthentication(String accessToken) {
        Authentication authentication = jwtTokenProvider.getAuthentication(accessToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    private void setResponse(HttpServletResponse response, JwtErrorCode errorCode) throws IOException {
        JSONObject json = new JSONObject();
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        json.put("code", errorCode.getCode());
        json.put("message", errorCode.getMessage());

        response.getWriter().print(json);
        response.getWriter().flush();
    }

    private void setForbiddenResponse(HttpServletResponse response, String message) throws IOException {
        JSONObject json = new JSONObject();
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);

        json.put("code", HttpServletResponse.SC_FORBIDDEN);
        json.put("message", message);

        response.getWriter().print(json);
        response.getWriter().flush();
    }
}
