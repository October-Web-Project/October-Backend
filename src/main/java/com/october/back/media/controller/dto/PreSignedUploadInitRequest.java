package com.october.back.media.controller.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PreSignedUploadInitRequest {

	private String originalFileName;
	private Long fileSize;
}