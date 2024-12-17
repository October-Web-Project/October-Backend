package com.october.back.user.entity;

import com.october.back.global.common.BaseEntity;
import com.october.back.user.service.dto.UserResponseDto;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Users extends BaseEntity {
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "nickname", unique = true, nullable = false)
    private String nickname;

    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @Enumerated(EnumType.STRING)
    private UserRole userRole;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true; // 기본값: 활성 상태

    @Builder(toBuilder = true)
    private Users(String name, String nickname, String email, UserRole userRole) {
        this.name = name;
        this.nickname = nickname;
        this.email = email;
        this.userRole = userRole;
    }

    public void updateNickname(String nickname) {
        this.nickname = nickname;
    }

    public void updateEmail(String email) {
        this.email = email;
    }
}
