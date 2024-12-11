package com.october.back.media.service.dto;

import com.october.back.media.entity.MediaType;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class PreSignedUploadInitServiceDto {

	private String originalFileName;
	private MediaType mediaType;
	private Long fileSize;
}
