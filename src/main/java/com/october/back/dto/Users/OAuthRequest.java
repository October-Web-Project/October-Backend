package com.october.back.dto.Users;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OAuthRequest {
    // OAuth 인증서버에 AT 요청
    private String authorizationCode; // 인가 코드
    private String provider;
    private String redirectUri; // 리다이렉트 URI
    private String clientId; // 네이버/카카오 클라이언트 ID
    private String clientSecret; // 네이버/카카오 클라이언트 Secret
}
