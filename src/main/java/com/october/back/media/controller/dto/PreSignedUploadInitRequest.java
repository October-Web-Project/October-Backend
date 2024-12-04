package com.october.back.media.controller.dto;

import com.october.back.media.entity.MediaType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PreSignedUploadInitRequest {

	private String originalFileName;
	private MediaType mediaType;
	private Long fileSize;
}
