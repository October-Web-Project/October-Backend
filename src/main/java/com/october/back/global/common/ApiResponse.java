package com.october.back.global.common;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ApiResponse<T> {

	private final HttpStatus httpStatus;
	private final int code;
	private final String message;
	private final T data;

	public static <T> ApiResponse<T> of(HttpStatus httpStatus, String message, T data) {
		return new ApiResponse<>(httpStatus, message, data);
	}

	public static <T> ApiResponse<T> of(HttpStatus httpStatus, T data) {
		return of(httpStatus, httpStatus.name(), data);
	}

	public static <T> ApiResponse<T> ok(T data) {
		return of(HttpStatus.OK, data);
	}

	public ApiResponse(HttpStatus httpStatus, String message, T data) {
		this.code = httpStatus.value();
		this.httpStatus = httpStatus;
		this.message = message;
		this.data = data;
	}
}
