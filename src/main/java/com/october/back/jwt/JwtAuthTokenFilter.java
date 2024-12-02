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
        log.info(path);
        if (path.contains("/swagger") || path.contains("/v3/api-docs")
                || path.startsWith("/auth") || path.startsWith("/error")
//                || path.startsWith("/normal/home") || path.startsWith("/property") || path.startsWith("/map")
//                || path.startsWith("/review/list") || path.startsWith("/review/detail")
//                || path.startsWith("/qna/list") || path.startsWith("/qna/detail") || path.startsWith("/qna/keyword")

        ) {
            filterChain.doFilter(request, response);
            return;
        }

        String AT = jwtTokenProvider.resolveAT(request);
        String RT = jwtTokenProvider.resolveRT(request);
        JwtErrorCode errorCode;

        try {
            // Access Token이 없고 Refresh Token만 있을 경우
            if (AT == null && RT != null) {
                if (path.contains("/refresh")) {
                    filterChain.doFilter(request, response);
                    return;
                }
            } else if (jwtTokenProvider.validateToken(AT)) {
                // Access Token 검증 성공 시
                this.setAuthentication(AT);

                // 추가: ROLE 검증
                String userRole = jwtTokenProvider.extractUserRoleFromToken(AT); // JWT에서 역할 추출
                if (path.startsWith("/admin") && !userRole.equals("ROLE_ADMIN")) {
                    // 관리자가 아닌 사용자가 /admin 경로 요청 시
                    setForbiddenResponse(response, "Admin role required");
                    return;
                }
                if (path.startsWith("/user") && !userRole.equals("ROLE_USER") && !userRole.equals("ROLE_ADMIN")) {
                    // /user 경로는 ROLE_USER 또는 ROLE_ADMIN만 접근 가능
                    setForbiddenResponse(response, "User or Admin role required");
                    return;
                }
            }

        } catch (MalformedJwtException e) {
            errorCode = JwtErrorCode.INVALID_JWT_TOKEN;
            setResponse(response, errorCode);
            return;
        } catch (ExpiredJwtException e) {
            errorCode = JwtErrorCode.JWT_TOKEN_EXPIRED;
            setResponse(response, errorCode);
            return;
        } catch (UnsupportedJwtException e) {
            errorCode = JwtErrorCode.UNSUPPORTED_JWT_TOKEN;
            setResponse(response, errorCode);
            return;
        } catch (IllegalArgumentException e) {
            errorCode = JwtErrorCode.EMPTY_JWT_CLAIMS;
            setResponse(response, errorCode);
            return;
        } catch (SignatureException e) {
            errorCode = JwtErrorCode.JWT_SIGNATURE_MISMATCH;
            setResponse(response, errorCode);
            return;
        } catch (RuntimeException e) {
            errorCode = JwtErrorCode.JWT_COMPLEX_ERROR;
            setResponse(response, errorCode);
            return;
        }

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
