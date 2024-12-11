package com.october.back.media.controller.dto;

import lombok.Getter;

@Getter
public class MediaResponse {

	private final String preSignedUrl;
	private final String message;

	private MediaResponse(String preSignedUrl, String message) {
		this.preSignedUrl = preSignedUrl;
		this.message = message;
	}

	public static MediaResponse of(String preSignedUrl, String message) {
		return new MediaResponse(preSignedUrl, message);
	}
}
