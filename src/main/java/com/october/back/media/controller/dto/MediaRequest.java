package com.october.back.media.controller.dto;

import com.october.back.media.entity.MediaType;
import com.october.back.media.service.dto.MediaServiceDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MediaRequest {

	private String fileName;
	private String uploadId;
	private Integer partNumber;
	private MediaType mediaType;

	public MediaServiceDto toMediaServiceDto() {
		return MediaServiceDto.builder()
                .fileName(fileName)
                .mediaType(mediaType)
                .build();
	}
}
