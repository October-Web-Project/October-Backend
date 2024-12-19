package com.october.back.security.oauth2.service;

import com.october.back.security.oauth2.service.dto.KakaoResponseDto;
import com.october.back.security.oauth2.service.dto.NaverResponseDto;
import com.october.back.security.oauth2.service.dto.OAuth2ResponseDto;
import com.october.back.user.entity.UserRole;
import com.october.back.user.entity.Users;
import com.october.back.user.entity.dto.UserDto;
import com.october.back.user.service.UserService;
import com.october.back.user.service.dto.UserRequestDto;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
    private final UserService userService;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2User oAuth2User = super.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        OAuth2ResponseDto oAuth2Response = null;
        if (registrationId.equals("naver")) {

            oAuth2Response = new NaverResponseDto(oAuth2User.getAttributes());
        } else if (registrationId.equals("kakao")) {

            oAuth2Response = new KakaoResponseDto(oAuth2User.getAttributes());
        } else {
            return null;
        }

        String username = oAuth2Response.getProvider() + " " + oAuth2Response.getProviderId();
        Optional<Users> existUser = userService.findByName(username);
        if (existUser.isEmpty()) {
            return createNewUser(oAuth2Response, username);
        }
        return updateExistingUser(existUser.get(), oAuth2Response);
    }
    private OAuth2User createNewUser(OAuth2ResponseDto oAuth2Response, String username) {
        UserRequestDto userRequestDto = UserRequestDto.builder()
                .name(username)
                .nickname(oAuth2Response.getNickname())
                .email(oAuth2Response.getEmail())
                .build();

        userService.createUser(userRequestDto);

        UserDto userDto = UserDto.builder()
                .name(username)
                .email(userRequestDto.getEmail())
                .nickname(userRequestDto.getNickname())
                .role(UserRole.USER)
                .build();
        return new CustomOAuth2User(userDto);
    }

    private OAuth2User updateExistingUser(Users existUser, OAuth2ResponseDto oAuth2Response) {
        existUser.updateEmail(oAuth2Response.getEmail());
        existUser.updateNickname(oAuth2Response.getNickname());
        userService.updateUser(existUser);

        UserDto userDto = UserDto.builder()
                .name(existUser.getName())
                .email(existUser.getEmail())
                .nickname(existUser.getNickname())
                .role(existUser.getUserRole())
                .build();
        return new CustomOAuth2User(userDto);
    }
}
