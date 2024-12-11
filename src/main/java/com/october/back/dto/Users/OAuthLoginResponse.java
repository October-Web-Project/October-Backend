package com.october.back.dto.Users;

import lombok.Getter;

@Getter
public class OAuthLoginResponse {
    private final String accessToken; // 클라이언트에 반환할 JWT 액세스 토큰
    private final String refreshToken; // 클라이언트에 반환할 리프레시 토큰
    private final String nickname; // 사용자 닉네임
    private final String statusMessage; // 로그인 상태 메시지 ("로그인 성공", "회원가입 완료", 등)

    public OAuthLoginResponse(String accessToken, String refreshToken, String nickname, String statusMessage) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.nickname = nickname;
        this.statusMessage = statusMessage;
    }
}
