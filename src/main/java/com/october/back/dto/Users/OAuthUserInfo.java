package com.october.back.dto.Users;
import lombok.Getter;

@Getter
public class OAuthUserInfo {
    //OAuth 로 가져온 사용자 정보 (사용자 프로필 이미지는 제외)
    private final String name; // 사용자 이름
    private final String nickname; // 사용자 닉네임
    private final String email; // 사용자 이메일
    private final String provider; // 로그인 제공자 (kakao, naver 등)


    public OAuthUserInfo(String name, String nickname, String email, String provider) {
        this.name = name;
        this.nickname = nickname;
        this.email = email;
        this.provider = provider;
    }

}
