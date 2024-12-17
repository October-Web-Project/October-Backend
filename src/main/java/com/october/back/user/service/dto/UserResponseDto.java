package com.october.back.user.service.dto;

import com.october.back.user.entity.UserRole;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserResponseDto {
    private Long id;
    private String name;
    private String email;
    private String nickname;
    private UserRole userRole;
    private Boolean isActive;

    @Builder
    public UserResponseDto(Long id, String name, String email, String nickname, UserRole userRole, boolean isActive) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.nickname = nickname;
        this.userRole = userRole;
        this.isActive = isActive;
    }
}