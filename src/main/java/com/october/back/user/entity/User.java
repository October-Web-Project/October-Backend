package com.october.back.user.entity;

import com.october.back.global.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "USERS")
public class User extends BaseEntity {

	@Column(name = "nickname", nullable = false, unique = true)
	private String nickname;

	@Column(name = "userID", nullable = false, unique = true)
	private String userID;

	@Column(name = "password", nullable = false)
	private String password;

	@Builder
	private User(String nickname, String userID, String password) {
		this.nickname = nickname;
		this.userID = userID;
		this.password = password;
	}
}
