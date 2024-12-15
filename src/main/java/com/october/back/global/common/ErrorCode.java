package com.october.back.global.common;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
@Getter
@AllArgsConstructor
public enum ErrorCode {

	// common
	INTERNAL_SERVER_ERROR(500, "C001", "internal server error"),
	INVALID_INPUT_VALUE(400, "C002", "invalid input type"),
	METHOD_NOT_ALLOWED(405, "C003", "method not allowed"),
	INVALID_TYPE_VALUE(400, "C004", "invalid type value"),
	BAD_CREDENTIALS(400, "C005", "bad credentials"),

	// member
	NOT_FOUND_USER(404, "M001", "사용자를 찾을 수 없습니다."),
	USER_ALREADY_DISABLED(400, "M002", "사용자가 이미 비활성화되었습니다."),
	// media
	NOT_FOUND_MEDIA(400, "M001", "미디어 URL을 찾을 수 없습니다."),

	// review

	// jwt
	INVALID_TOKEN(401, "J001", "유효하지 않은 토큰입니다."),
	EXPIRED_TOKEN(401, "J002", "토큰이 만료되었습니다."),
	UNSUPPORTED_TOKEN(401, "J003", "지원되지 않는 토큰 형식입니다."),
	EMPTY_TOKEN(401, "J004", "토큰이 비어 있습니다."),
	ACCESS_DENIED(403, "J005", "접근이 거부되었습니다.");

	private int status;
	private final String code;
	private final String message;
}
