package com.october.back.security.oauth2.serivce;

import com.october.back.security.oauth2.serivce.dto.KakaoResponseDto;
import com.october.back.security.oauth2.serivce.dto.NaverResponseDto;
import com.october.back.security.oauth2.serivce.dto.OAuth2ResponseDto;
import com.october.back.user.entity.dto.UserDto;
import com.october.back.user.service.UserService;
import com.october.back.user.service.dto.UserRequestDto;
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

        UserDto user = UserDto.builder()
                .name(username)
                .email(oAuth2Response.getEmail())
                .nickname(oAuth2Response.getNickname())
                .build();

        UserRequestDto userRequestDto = UserRequestDto.builder()
                .name(username)
                .nickName(oAuth2Response.getNickname())
                .email(oAuth2Response.getEmail())
                .build();

        userService.createUser(userRequestDto);

        return new CustomOAuth2User(user);
    }
}
