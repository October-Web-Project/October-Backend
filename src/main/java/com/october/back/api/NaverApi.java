package com.october.back.api;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.october.back.dto.Users.OAuthTokenResponse;
import com.october.back.dto.Users.OAuthUserInfo;
import com.october.back.error.ErrorCode;
import com.october.back.error.exception.UnAuthorizedException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class NaverApi {

    @Value("${spring.security.oauth2.client.registration.naver.client-id}")
    private String naverClientId;

    @Value("${spring.security.oauth2.client.registration.naver.client-secret}")
    private String naverClientSecret;

    @Value("${spring.security.oauth2.client.registration.naver.redirect-uri}")
    private String naverRedirectUri;

    private final RestTemplate restTemplate;


    //네이버 엔드포인트
    private static final String REQ_ACCESS_TOKEN_URL = "https://nid.naver.com/oauth2.0/token";
    private static final String REQ_USER_INFO_URL = "https://openapi.naver.com/v1/nid/me";

    //AT 발급
    public OAuthTokenResponse getAccessToken(String authorizationCode) {
        // HTTP Header 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        // HTTP 요청 Body 설정
        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
        parameters.add("grant_type", "authorization_code");
        parameters.add("client_id", naverClientId);
        parameters.add("client_secret", naverClientSecret);
        parameters.add("redirect_uri", naverRedirectUri);
        parameters.add("code", authorizationCode);

        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(parameters, headers);

        // POST 요청 보내기
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(REQ_ACCESS_TOKEN_URL, requestEntity, String.class);

        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            // JSON 응답에서 Access Token을 파싱하여 반환
            JsonObject jsonObject = JsonParser.parseString(responseEntity.getBody()).getAsJsonObject();

            // 생성자를 사용하여 OAuthTokenResponse 객체 생성
            String accessToken = jsonObject.get("access_token").getAsString();
            String refreshToken = jsonObject.has("refresh_token") ? jsonObject.get("refresh_token").getAsString() : null;
            Long expiresIn = jsonObject.has("expires_in") ? jsonObject.get("expires_in").getAsLong() : null;
            String tokenType = jsonObject.has("token_type") ? jsonObject.get("token_type").getAsString() : null;

            return new OAuthTokenResponse(accessToken, refreshToken, expiresIn, tokenType);
        } else {
            throw new UnAuthorizedException("Failed to get access token from Naver!", ErrorCode.NAVER_ACCESS_TOKEN_FAILED);
        }
    }


    //정보 조회
    public OAuthUserInfo getUserInfo(String accessToken) {
        // HTTP Header 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);

        HttpEntity<String> requestEntity = new HttpEntity<>(headers);

        // GET 요청 보내기
        ResponseEntity<String> responseEntity = restTemplate.exchange(REQ_USER_INFO_URL, HttpMethod.GET, requestEntity, String.class);

        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            // JSON 응답에서 사용자 정보 파싱하여 반환
            JsonObject jsonObject = JsonParser.parseString(responseEntity.getBody()).getAsJsonObject();
            JsonObject response = jsonObject.getAsJsonObject("response");
            String name = response.get("name").getAsString();
            String email = response.get("email").getAsString();
            String nickname = response.get("nickname").getAsString();

            // 생성자를 사용하여 OAuthUserInfo 객체 생성
            return new OAuthUserInfo(name, nickname, email, "naver");
        } else {
            throw new UnAuthorizedException("Failed to get user info from Naver!", ErrorCode.NAVER_USER_INFO_FAILED);
        }
    }


}
