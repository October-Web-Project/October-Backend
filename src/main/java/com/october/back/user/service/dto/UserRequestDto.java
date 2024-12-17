package com.october.back.user.service.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserRequestDto {
    private String name;
    private String email;
    private String nickname;

    @Builder
    public UserRequestDto(String name, String email, String nickname) {
        this.name = name;
        this.email = email;
        this.nickname = nickname;
    }
}