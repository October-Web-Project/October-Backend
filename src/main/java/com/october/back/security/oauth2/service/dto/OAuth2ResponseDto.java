package com.october.back.security.oauth2.service.dto;

public interface OAuth2ResponseDto {

    String getProvider();
    String getProviderId();
    String getEmail();
    String getName();
    String getNickname();
}