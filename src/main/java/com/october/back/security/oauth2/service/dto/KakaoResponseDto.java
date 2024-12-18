package com.october.back.security.oauth2.service.dto;

import java.util.Map;

public class KakaoResponseDto implements OAuth2ResponseDto {

    private final Map<String, Object> attributes;
    private final Map<String, Object> kakaoAccount;
    private final Map<String, Object> properties;

    public KakaoResponseDto(Map<String, Object> attributes) {
        this.attributes = attributes;
        this.kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
        this.properties = (Map<String, Object>) attributes.get("properties");
    }

    @Override
    public String getProvider() {
        return "kakao";
    }

    @Override
    public String getProviderId() {
        return attributes.get("id").toString();
    }

    @Override
    public String getEmail() {
        return kakaoAccount.get("email").toString();
    }

    @Override
    public String getName() {
        if (kakaoAccount != null && kakaoAccount.get("name") != null) {
            return kakaoAccount.get("name").toString();
        }
        return getNickname();
    }

    @Override
    public String getNickname() {
        return properties.get("nickname").toString();
    }
}

