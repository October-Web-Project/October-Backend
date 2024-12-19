package com.october.back.security.oauth2.service.dto;

import java.util.Map;

public class NaverResponseDto implements OAuth2ResponseDto {

    private final Map<String, Object> attribute;

    public NaverResponseDto(Map<String, Object> attribute) {

        this.attribute = (Map<String, Object>) attribute.get("response");
    }

    @Override
    public String getProvider() {
        return "naver";
    }

    @Override
    public String getProviderId() {
        return attribute.get("id").toString();
    }

    @Override
    public String getEmail() {
        return attribute.get("email").toString();
    }

    @Override
    public String getName() {
        if (attribute != null && attribute.get("name") != null) {
            return attribute.get("name").toString();
        }
        return getNickname();
    }

    @Override
    public String getNickname() {
        return attribute.get("name").toString();
    }
}
