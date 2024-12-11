package com.october.back.service.ServiceImpl;


import com.october.back.api.KakaoApi;
import com.october.back.api.NaverApi;
import com.october.back.dto.Users.OAuthLoginResponse;
import com.october.back.dto.Users.OAuthTokenResponse;
import com.october.back.dto.Users.OAuthUserInfo;
import com.october.back.entity.Users;
import com.october.back.error.ErrorCode;
import com.october.back.error.exception.ClientException;
import com.october.back.error.exception.UnAuthorizedException;
import com.october.back.jwt.JwtTokenProvider;
import com.october.back.repo.UserRepo;
import com.october.back.service.Service.UserService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);


    private final UserRepo userRepo;
    private final NaverApi naverApi;
    private final KakaoApi kakaoApi;
    private final RedisService redisService;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public OAuthLoginResponse oauthLogin(String authorizationCode, String provider) {
        OAuthTokenResponse tokenResponse;
        OAuthUserInfo userInfo;

        try {
            if ("naver".equalsIgnoreCase(provider)) {
                tokenResponse = naverApi.getAccessToken(authorizationCode);
                userInfo = naverApi.getUserInfo(tokenResponse.getAccessToken());
            } else if ("kakao".equalsIgnoreCase(provider)) {
                tokenResponse = kakaoApi.getAccessToken(authorizationCode);
                userInfo = kakaoApi.getUserInfo(tokenResponse.getAccessToken());
            } else {
                throw new UnAuthorizedException("Unsupported provider: " + provider, ErrorCode.UNAUTHORIZED_EXCEPTION);
            }
        } catch (Exception e) {
            if ("naver".equalsIgnoreCase(provider)) {
                throw new ClientException("Failed to process Naver login", ErrorCode.NAVER_USER_INFO_FAILED);
            } else if ("kakao".equalsIgnoreCase(provider)) {
                throw new ClientException("Failed to process Kakao login", ErrorCode.KAKAO_USER_INFO_FAILED);
            } else {
                throw new UnAuthorizedException("Unknown error during login", ErrorCode.UNAUTHORIZED_EXCEPTION);
            }
        }


        // 사용자 정보 확인 및 저장/업데이트
        Users user = userRepo.findByEmail(userInfo.getEmail())
                .orElseGet(() -> userRepo.save(new Users(
                        userInfo.getName(),
                        userInfo.getNickname(),
                        userInfo.getEmail(),
                        "ROLE_USER"
                )));

        if (user.getId() == null || user.getNickName() == null || user.getEmail() == null) {
            throw new IllegalStateException("User data is incomplete for JWT generation.");
        }

        String userRole = "ROLE_USER";

        // JWT 생성
        String accessToken = jwtTokenProvider.createAccessToken(
                user.getId().toString(),
                user.getUserRole(),
                user.getNickName(),
                user.getEmail(),
                user.getName()
        );


        String refreshToken = jwtTokenProvider.createRefreshToken(
                user.getId().toString(),
                user.getUserRole(),
                user.getNickName(),
                user.getEmail(),
                user.getName()
        );

        // Redis에 Refresh Token 저장
        redisService.save("RT:" + user.getId(), refreshToken, 7 * 24 * 60 * 60L);

        return new OAuthLoginResponse(accessToken, refreshToken, user.getNickName(), "로그인 성공");
    }


    @Override
    public void logout(Long userId) {
        if (!userRepo.existsById(userId)) {
            throw new ClientException("User not found with ID: " + userId, ErrorCode.NOT_FOUND_EXCEPTION);
        }

        String redisKey = "RT:" + userId;
        if (redisService.exists(redisKey)) {
            redisService.delete(redisKey);
        }
    }

    @Override
    public void deleteUser(Long userId) {
        Users user = userRepo.findById(userId)
                .orElseThrow(() -> new ClientException("User not found with ID: " + userId, ErrorCode.NOT_FOUND_EXCEPTION));

        // JWT 블랙리스트에 추가
        String accessToken = jwtTokenProvider.createAccessToken(
                user.getId().toString(), "ROLE_USER", user.getNickName(), user.getEmail(), user.getName());
        redisService.save("BL:" + accessToken, "", 7 * 24 * 60 * 60L);

        userRepo.delete(user);
    }

    @Override
    public OAuthUserInfo getUserInfo(Long userId) {
        Users user = userRepo.findById(userId)
                .orElseThrow(() -> new ClientException(ErrorCode.NOT_FOUND_EXCEPTION));

        return new OAuthUserInfo(user.getName(), user.getNickName(), user.getEmail(), "");
    }


    @Override
    public void updateUser(Long userId, String nickname) {

        if (userRepo.existsByNickName(nickname)) {
            throw new ClientException(ErrorCode.DUPLICATE_EXCEPTION);
        }

        Users user = userRepo.findById(userId)
                .orElseThrow(() -> {
                    log.error("User not found with id: {}", userId);
                    return new ClientException(ErrorCode.NOT_FOUND_EXCEPTION);
                });

        user.changeNickname(nickname);
        userRepo.save(user);
    }



    @Override
    public OAuthLoginResponse reissueToken(String refreshToken) {
        // 리프레시 토큰 유효성 검증
        jwtTokenProvider.validateToken(refreshToken);

        // 리프레시 토큰에서 사용자 정보 추출
        Long userId = jwtTokenProvider.getUserId(refreshToken);
        Users user = userRepo.findById(userId)
                .orElseThrow(() -> new ClientException(ErrorCode.NOT_FOUND_EXCEPTION));

        // 새로운 액세스 토큰 생성
        String newAccessToken = jwtTokenProvider.createAccessToken(
                user.getId().toString(), "ROLE_USER", user.getNickName(), user.getEmail(), user.getName());

        return new OAuthLoginResponse(newAccessToken, refreshToken, user.getNickName(), "토큰 재발급 성공");
    }


}
