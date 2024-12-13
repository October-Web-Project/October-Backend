package com.october.back.security.oauth2.serivce.dto;

public interface OAuth2ResponseDto {

    String getProvider();
    String getProviderId();
    String getEmail();
    String getName();
    String getNickname();
}