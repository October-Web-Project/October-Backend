package com.october.back.global.common;

import lombok.Getter;

@Getter
public class ErrorResponse {

	private final int status;
	private final String code;
	private final String message;

	public ErrorResponse(final ErrorCode code) {
		this.status = code.getStatus();
		this.code = code.getCode();
		this.message = code.getMessage();
	}
}