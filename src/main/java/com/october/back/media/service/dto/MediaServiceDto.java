package com.october.back.media.service.dto;

import com.october.back.media.entity.MediaType;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class MediaServiceDto {
	private String fileName;
	private MediaType mediaType;
}
