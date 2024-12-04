package com.october.back.media.controller.dto;

import com.october.back.media.service.dto.FinishUploadServiceDto;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class FinishUploadRequest {

	private String fileName;
	private String uploadId;
	private List<Part> parts;

	@Getter
	@Setter
	public static class Part {

		private Integer partNumber;
		private String eTag;
	}

	public FinishUploadServiceDto toFinishUploadServiceDto() {
		return FinishUploadServiceDto.builder()
				.fileName(this.fileName)
				.uploadId(this.uploadId)
				.parts(this.parts.stream()
						.map(part -> FinishUploadServiceDto.Part.builder()
								.partNumber(part.getPartNumber())
								.eTag(part.getETag())
								.build())
						.collect(Collectors.toList()))
				.build();
	}
}
