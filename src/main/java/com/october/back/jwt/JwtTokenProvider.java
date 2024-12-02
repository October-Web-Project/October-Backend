package com.october.back.jwt;

import com.october.back.error.ErrorCode;
import com.october.back.error.exception.ClientException;
import com.october.back.error.exception.UnAuthorizedException;
import com.october.back.entity.Users;
import com.october.back.repo.UserRepo;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;
import java.security.Key;
import java.util.*;

@Component
@RequiredArgsConstructor
public class JwtTokenProvider {
    private final CustomUserDetailService customUserDetailService;
    private final UserRepo userRepo;

    @Value("${jwt.secretKey}")
    private String secretKey;

    // 액세스 토큰 유효시간
    @Value("${jwt.accessTokenExpiration}")
    private long accessTokenValidTime;

    // 리프레시 토큰 유효시간
    @Value("${jwt.refreshTokenExpiration}")
    private long refreshTokenValidTime;

    public String resolveAT(HttpServletRequest request) {
        if (request.getHeader("Authorization") != null )
            return request.getHeader("Authorization").substring(7);
        return null;
    }

    public String resolveRT(HttpServletRequest request) {
        if (request.getHeader("refreshToken") != null )
            return request.getHeader("refreshToken").substring(7);
        return null;
    }
    public boolean validateToken(String jwtToken) {
        try {
            Key key = Keys.hmacShaKeyFor(secretKey.getBytes());
            Jws<Claims> claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(jwtToken);

            return !claims.getBody().getExpiration().before(new Date());
        } catch (JwtException e) {
            throw new UnAuthorizedException("토큰 만료",ErrorCode.UNAUTHORIZED_EXCEPTION);
        }
    }
    public UsernamePasswordAuthenticationToken getAuthentication(String token) {
        Long userId = this.getUserId(token); // Long 타입으로 사용자 ID를 추출
        UserDetails userDetails = customUserDetailService.loadUserById(userId); // 사용자 ID로 조회
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }
    public Long getUserId(String refreshToken) {
        String userIdStr = Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(secretKey.getBytes()))
                .build()
                .parseClaimsJws(refreshToken)
                .getBody()
                .getSubject();

        return Long.valueOf(userIdStr);
    }
    public Map<String, String> getTokenBody(String token) {
        JwtParser jwtParser = Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(secretKey.getBytes()))
                .build();
        Claims claims = jwtParser.parseClaimsJws(token).getBody();

        Map<String, String> claimsBody = new HashMap<>();
        claimsBody.put("role", (String) claims.get("role"));
        claimsBody.put("nickName", (String) claims.get("nickName"));
        return claimsBody;
    }
    public String createAccessToken(String userId, String userRole, String nickName) {
            return this.createToken(userId, userRole, nickName, accessTokenValidTime);
    }

    // Refresh Token 생성.
    public String createRefreshToken(String userId, String userRole, String nickName) {
            return this.createToken(userId, userRole, nickName, refreshTokenValidTime);
    }

    // Create token
    private String createToken(String userId, String userRole, String nickName, long tokenValid) {
        Claims claims = Jwts.claims().setSubject(userId);
        claims.put("role", userRole);
        claims.put("nickName",nickName);

        Key key = Keys.hmacShaKeyFor(secretKey.getBytes());
        Date date = new Date();

        return Jwts.builder()
                .setClaims(claims) // 발행 유저 정보 저장
                .setIssuedAt(date) // 발행 시간 저장
                .setExpiration(new Date(date.getTime() + tokenValid)) // 토큰 유효 시간 저장
                .signWith(key, SignatureAlgorithm.HS256) // 해싱 알고리즘 및 키 설정
                .compact(); // 생성
    }


    public String extractUserRoleFromToken(String token){
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(secretKey.getBytes()))
                .build()
                .parseClaimsJws(token)
                .getBody();


        return (String) claims.get("role");
    }


    public String refreshAccessToken(String refreshToken) {
        this.validateToken(refreshToken);

        Long userId = this.getUserId(refreshToken);

        Users userEntity = userRepo.findById(userId)
                .orElseThrow(() -> new ClientException("force re-login", ErrorCode.NOT_FOUND_EXCEPTION));

        String userRole = extractUserRoleFromToken(refreshToken);

        return this.createAccessToken(userEntity.getId().toString(), userRole, userEntity.getNickName());
    }
    //redis 사용 연계로 블랙리스트 구현
    //public void expireToken(){}

    public void setHeaderAccessToken(HttpServletResponse response, String accessToken) {
        response.setHeader("Authorization", "bearer "+ accessToken);
    }

    // 리프레시 토큰 헤더 설정
    public void setHeaderRefreshToken(HttpServletResponse response, String refreshToken) {
        response.setHeader("refreshToken", "bearer "+ refreshToken);
    }
    public String resolveAccessToken(HttpServletRequest request) {
        if (request.getHeader("Authorization") != null )
            return request.getHeader("Authorization").substring(7);
        return null;
    }

    // Request의 Header에서 RefreshToken 값을 가져옵니다. "refreshToken" : "token"
    public String resolveRefreshToken(HttpServletRequest request) {
        if (request.getHeader("refreshToken") != null )
            return request.getHeader("refreshToken").substring(7);
        return null;
    }
}
