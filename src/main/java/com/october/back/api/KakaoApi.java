package com.october.back.api;

import com.october.back.dto.Users.OAuthTokenResponse;
import com.october.back.dto.Users.OAuthUserInfo;
import com.october.back.error.ErrorCode;
import com.october.back.error.exception.UnAuthorizedException;
import com.october.back.service.ServiceImpl.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;


@Component
@RequiredArgsConstructor
public class KakaoApi {

    @Value("${spring.security.oauth2.client.registration.kakao.client-id}")
    private String kakaoClientId;

    @Value("${spring.security.oauth2.client.registration.kakao.redirect-uri}")
    private String kakaoRedirectUri;

    @Value("${spring.security.oauth2.client.registration.kakao.client-secret}")
    private String kakaoClientSecret;

    private final RestTemplate restTemplate;

    //Kakao OAuth 엔드포인트
    private static final String REQ_ACCESS_TOKEN_URL = "https://kauth.kakao.com/oauth/token";
    private static final String REQ_USER_INFO_URL = "https://kapi.kakao.com/v2/user/me";

    private static final Logger log = LoggerFactory.getLogger(KakaoApi.class);

    // AT 발급
    public OAuthTokenResponse getAccessToken(String authorizationCode) {
        // HTTP Header 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        // HTTP 요청 Body 설정
        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
        parameters.add("grant_type", "authorization_code");
        parameters.add("client_id", kakaoClientId);
        parameters.add("redirect_uri", kakaoRedirectUri);
        parameters.add("client_secret", kakaoClientSecret);
        parameters.add("code", authorizationCode);

        log.debug("Access Token Request Parameters: {}", parameters);
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(parameters, headers);

        // POST 요청 보내기
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(REQ_ACCESS_TOKEN_URL, requestEntity, String.class);


        log.info("Kakao Access Token Response Status: {}", responseEntity.getStatusCode()); // 디버깅 추가
        log.debug("Kakao Access Token Response Body: {}", responseEntity.getBody());
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
            throw new UnAuthorizedException("Failed to get access token from Kakao!", ErrorCode.KAKAO_ACCESS_TOKEN_FAILED);
        }
    }
    public OAuthUserInfo getUserInfo(String accessToken) {
        // HTTP Header 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);

        // 사용자 정보 요청
        log.debug("Requesting Kakao User Info with Access Token: {}", accessToken);
        log.info("Kakao User Info URL: {}", REQ_USER_INFO_URL);
        HttpEntity<String> requestEntity = new HttpEntity<>(headers);

        // GET 요청 보내기
        ResponseEntity<String> responseEntity = restTemplate.exchange(REQ_USER_INFO_URL, HttpMethod.GET, requestEntity, String.class);

        log.debug("Kakao User Info Response Body: {}", responseEntity.getBody());

        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            // JSON 응답에서 사용자 정보 파싱하여 반환
            JsonObject jsonObject = JsonParser.parseString(responseEntity.getBody()).getAsJsonObject();
            log.debug("Parsed Kakao User Info JSON: {}", jsonObject);

            // 필드 존재 여부 확인
            if (!jsonObject.has("kakao_account")) {
                log.error("Missing 'kakao_account' in response JSON");
            }

            JsonObject kakaoAccount = jsonObject.has("kakao_account") ? jsonObject.getAsJsonObject("kakao_account") : null;

            // 이름 정보 파싱
            String name = "Unknown";
            if (kakaoAccount != null && kakaoAccount.has("name")) {
                name = kakaoAccount.get("name").getAsString();
                log.debug("Name found in 'kakao_account': {}", name);
            } else {
                log.warn("Name not found in Kakao response. Setting default value. JSON: {}", jsonObject);
            }

            // 닉네임 정보 파싱
            String nickname = "Unknown";
            if (kakaoAccount != null && kakaoAccount.has("profile") && kakaoAccount.getAsJsonObject("profile").has("nickname")) {
                nickname = kakaoAccount.getAsJsonObject("profile").get("nickname").getAsString();
                log.debug("Nickname found in 'kakao_account.profile': {}", nickname);
            } else {
                log.warn("Nickname not found in Kakao response. Setting default value. JSON: {}", jsonObject);
            }

            // 이메일 정보 파싱
            String email = kakaoAccount != null && kakaoAccount.has("email") ? kakaoAccount.get("email").getAsString() : "Unknown";

            // 생성자를 사용하여 OAuthUserInfo 객체 생성
            return new OAuthUserInfo(name, nickname, email, "kakao");
        } else {
            throw new UnAuthorizedException("Failed to get user info from Kakao!", ErrorCode.KAKAO_USER_INFO_FAILED);
        }
    }



}
