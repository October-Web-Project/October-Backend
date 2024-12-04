package com.october.back.global.common;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ApiResponse {

	private final HttpStatus httpStatus;
	private final int code;
	private final String message;

	public ApiResponse(HttpStatus httpStatus, String message) {
		this.code = httpStatus.value();
		this.httpStatus = httpStatus;
		this.message = message;
	}
}
