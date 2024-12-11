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

	// media
	NOT_FOUND_MEDIA(400, "M001", "미디어 URL을 찾을 수 없습니다.");

	// review

	private int status;
	private final String code;
	private final String message;
}
