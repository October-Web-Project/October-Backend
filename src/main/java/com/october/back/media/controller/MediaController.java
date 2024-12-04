package com.october.back.media.controller;

import com.amazonaws.services.s3.model.CompleteMultipartUploadResult;
import com.amazonaws.services.s3.model.InitiateMultipartUploadResult;
import com.october.back.media.controller.dto.FinishUploadRequest;
import com.october.back.media.controller.dto.MediaRequest;
import com.october.back.media.controller.dto.MediaResponse;
import com.october.back.media.controller.dto.PreSignedUploadInitRequest;
import com.october.back.media.entity.MediaType;
import com.october.back.media.service.MediaService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/media")
@RequiredArgsConstructor
public class MediaController {

	private final MediaService mediaService;

	@PostMapping("/{type}/initiate-upload")
	public InitiateMultipartUploadResult initiateUpload(@PathVariable(name = "type") MediaType mediaType,
														@RequestBody PreSignedUploadInitRequest request) {
		return mediaService.initiateMultipartUploadResult(mediaType, request);
	}

	/**
	 * @param mediaType
	 * @param request
	 * @return
	 */
	@PostMapping("/{type}/presigned-url")
	public MediaResponse getPreSignedUrl(@PathVariable(name = "type") MediaType mediaType,
										 @RequestBody MediaRequest request) {
		String preSignedUrl = mediaService.getPreSignedUrl(mediaType, request.toMediaServiceDto());
		return MediaResponse.of(preSignedUrl, "PreSignedURL 발급 성공");
	}

	/**
	 * @param mediaType
	 * @param request
	 * @return
	 */
	@PostMapping("/{type}/complete-upload")
	public CompleteMultipartUploadResult completeMultipartUpload(@PathVariable(name = "type") MediaType mediaType,
																 @RequestBody FinishUploadRequest request) {
		return mediaService.uploadComplete(mediaType, request.toFinishUploadServiceDto());
	}
}
