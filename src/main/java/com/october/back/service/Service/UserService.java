package com.october.back.service.Service;

import com.october.back.dto.Users.OAuthLoginResponse;
import com.october.back.dto.Users.OAuthUserInfo;

public interface UserService {
    OAuthLoginResponse oauthLogin(String authorizationCode, String provider);

    void logout(Long userId);

    void deleteUser(Long userId);

    OAuthUserInfo getUserInfo(Long userId);

    void updateUser(Long userId, String nickname);

    OAuthLoginResponse reissueToken(String refreshToken);


}
