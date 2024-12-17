package com.october.back.user.entity.dto;

import com.october.back.user.entity.UserRole;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDto {

    private String name;
    private String nickname;
    private String email;
    private UserRole role;

    @Builder
    public UserDto(String name, String nickname, String email, UserRole role) {
        this.name = name;
        this.nickname = nickname;
        this.email = email;
        this.role = role;
    }
}