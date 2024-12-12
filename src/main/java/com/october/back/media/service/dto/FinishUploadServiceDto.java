package com.october.back.media.service.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class FinishUploadServiceDto {

	private String uploadId;
	private String fileName;
	private List<Part> parts;

	@Getter
	@Setter
	@Builder
	@AllArgsConstructor(access = AccessLevel.PROTECTED)
	public static class Part {
		private Integer partNumber;
		private String eTag;
	}
}
