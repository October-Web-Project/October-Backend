package com.october.back.media.controller;

import com.amazonaws.services.s3.model.CompleteMultipartUploadResult;
import com.amazonaws.services.s3.model.InitiateMultipartUploadResult;
import com.october.back.media.controller.dto.FinishUploadRequest;
import com.october.back.media.controller.dto.MediaRequest;
import com.october.back.media.controller.dto.MediaResponse;
import com.october.back.media.controller.dto.PreSignedUploadInitRequest;
import com.october.back.media.service.MediaService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/media")
@RequiredArgsConstructor
public class MediaController {

	private final MediaService mediaService;

	/**
	 * @param request
	 * @return
	 */
	@PostMapping("/initiate-upload")
	public InitiateMultipartUploadResult initiateUpload(@RequestBody PreSignedUploadInitRequest request) {
		return mediaService.initiateMultipartUploadResult(request);
	}

	/**
	 * @param request
	 * @return
	 */
	@PostMapping("/presigned-url")
	public MediaResponse getPreSignedUrl(@RequestBody MediaRequest request) {
		String preSignedUrl = mediaService.getPreSignedUrl(request.toMediaServiceDto());
		return MediaResponse.of(preSignedUrl, "PreSignedURL 발급 성공");
	}

	/**
	 * @param request
	 * @return
	 */
	@PostMapping("/complete-upload")
	public CompleteMultipartUploadResult completeMultipartUpload(@RequestBody FinishUploadRequest request) {
		return mediaService.uploadComplete(request.toFinishUploadServiceDto());
	}
}