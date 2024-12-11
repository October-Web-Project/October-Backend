package com.october.back.controller.Users;

import com.october.back.dto.Users.OAuthLoginResponse;
import com.october.back.jwt.JwtTokenProvider;
import com.october.back.service.Service.UserService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpHeaders;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/normal/users")
public class NormalUsersController {

    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping("/auth/login")
    public ResponseEntity<Map<String, String>> login(@RequestParam String authorizationCode,
                                                     @RequestParam String provider) {
        OAuthLoginResponse loginResponse = userService.oauthLogin(authorizationCode, provider);

        // HttpHeaders로 헤더 구성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", loginResponse.getAccessToken());
        headers.add("refreshToken", loginResponse.getRefreshToken());

        // 응답 본문
        Map<String, String> body = new HashMap<>();
        body.put("message", "로그인 성공, 헤더에 토큰 확인");

        return new ResponseEntity<>(body, headers, HttpStatus.OK);
    }

    @PostMapping("/logout")
    public ResponseEntity<Map<String, String>> logout(@RequestHeader("Authorization") String accessToken) {
        Long userId = jwtTokenProvider.getUserId(accessToken); // 접두사 없는 액세스 토큰 처리
        userService.logout(userId);


        // HttpHeaders 구성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", ""); // 로그아웃 시 Authorization 헤더 제거

        // 응답 본문 구성
        Map<String, String> body = new HashMap<>();
        body.put("message", "로그아웃 성공");

        return new ResponseEntity<>(body, headers, HttpStatus.OK);
    }


    @DeleteMapping("/delete")
    public ResponseEntity<Map<String, String>> deleteUser(@RequestHeader("Authorization") String accessToken) {
        Long userId = jwtTokenProvider.getUserId(accessToken); // 접두사 없는 액세스 토큰 처리
        userService.deleteUser(userId);

        // HttpHeaders 구성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", ""); // 사용자 탈퇴 시 Authorization 헤더 제거

        // 응답 본문 구성
        Map<String, String> body = new HashMap<>();
        body.put("message", "탈퇴 성공");

        return new ResponseEntity<>(body, headers, HttpStatus.OK);
    }


    @PatchMapping("/updateNickname")
    public ResponseEntity<Map<String, String>> updateNickname(
            @RequestHeader("Authorization") String accessToken,
            @RequestBody Map<String, String> requestBody) {

        Long userId = jwtTokenProvider.getUserId(accessToken); // 접두사 없는 액세스 토큰 처리
        String nickname = requestBody.get("nickname");

        userService.updateUser(userId, nickname);

        // 응답 본문
        Map<String, String> responseBody = new HashMap<>();
        responseBody.put("message", "닉네임 변경 성공");
        responseBody.put("nickname", nickname);

        return ResponseEntity.ok(responseBody);
    }

    @PostMapping("/reissue")
    public ResponseEntity<Map<String, String>> reissueToken(@RequestHeader("refreshToken") String refreshToken) {
        // 서비스 호출을 통해 새로운 액세스 토큰 생성
        OAuthLoginResponse reissueResponse = userService.reissueToken(refreshToken); // 접두사 없는 리프레시 토큰 처리

        // HttpHeaders로 헤더 구성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", reissueResponse.getAccessToken()); // 새로운 Access Token
        headers.add("refreshToken", reissueResponse.getRefreshToken()); // 기존 Refresh Token

        // 응답 본문
        Map<String, String> body = new HashMap<>();
        body.put("message", "토큰 재발급 완료, 헤더에 토큰 확인");

        return new ResponseEntity<>(body, headers, HttpStatus.OK);
    }


}
