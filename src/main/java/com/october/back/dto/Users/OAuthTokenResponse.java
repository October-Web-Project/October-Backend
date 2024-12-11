package com.october.back.dto.Users;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OAuthTokenResponse {
    //OAuth 인증서버에서 토큰 발급 후 응답 형식
    private String accessToken; // 발급받은 액세스 토큰
    private String refreshToken; // 발급받은 리프레시 토큰
    private Long expiresIn; // 액세스 토큰 만료 시간 (초 단위)
    private String tokenType; // 토큰 유형 (일반적으로 "Bearer")

    public OAuthTokenResponse(String accessToken, String refreshToken, Long expiresIn, String tokenType) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.expiresIn = expiresIn;
        this.tokenType = tokenType;
    }
}
