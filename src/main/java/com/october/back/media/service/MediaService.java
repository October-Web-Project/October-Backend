package com.october.back.media.service;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.Headers;
import com.amazonaws.services.s3.model.*;
import com.october.back.media.controller.dto.PreSignedUploadInitRequest;
import com.october.back.media.entity.Media;
import com.october.back.media.repository.MediaRepository;
import com.october.back.media.service.dto.FinishUploadServiceDto;
import com.october.back.media.service.dto.MediaServiceDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.net.URLConnection;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MediaService {

	@Value("${cloud.aws.s3.bucket}")
	private String bucket;

	private final AmazonS3 amazonS3;
	private final MediaRepository mediaRepository;

	/**
	 * @param request
	 * @return
	 */
	public InitiateMultipartUploadResult initiateMultipartUploadResult(PreSignedUploadInitRequest request) {
		ObjectMetadata objectMetadata = new ObjectMetadata();
		objectMetadata.setContentLength(request.getFileSize());
		objectMetadata.setContentType(URLConnection.guessContentTypeFromName(request.getOriginalFileName()));

		return amazonS3.initiateMultipartUpload(
				new InitiateMultipartUploadRequest(bucket, createPath(request.getOriginalFileName()), objectMetadata)
		);
	}


	/**
	 * @param dto
	 * @return
	 */
	public String getPreSignedUrl(MediaServiceDto dto) {
		String path = createPath(dto.getFileName());
		GeneratePresignedUrlRequest generatePreSignedUrlRequest = getGeneratePreSignedUrlRequest(bucket, path);
		URL url = amazonS3.generatePresignedUrl(generatePreSignedUrlRequest);
		return url.toString();
	}

	/**
	 * @param dto
	 * @return
	 */
	public CompleteMultipartUploadResult uploadComplete(FinishUploadServiceDto dto) {
		List<PartETag> partETagList = dto.getParts().stream()
				.map(part -> new PartETag(part.getPartNumber(), part.getETag()))
				.collect(Collectors.toList());

		String path = createPath(dto.getFileName());

		CompleteMultipartUploadRequest completeMultipartUploadRequest = new CompleteMultipartUploadRequest(
				bucket,
				path,
				dto.getUploadId(),
				partETagList
		);

		Media media = Media.builder()
				.clientUploadFileName(dto.getFileName())
				.serverStoredUrl(path)
				.build();
		mediaRepository.save(media);
		return amazonS3.completeMultipartUpload(completeMultipartUploadRequest);
	}

	/**
	 * 파일 업로드용(PUT) presigned url 생성
	 * @param bucket 버킷 이름
	 * @param fileName S3 업로드용 파일 이름
	 * @return presigned url
	 */
	private GeneratePresignedUrlRequest getGeneratePreSignedUrlRequest(String bucket, String fileName) {
		GeneratePresignedUrlRequest generatePresignedUrlRequest =
				new GeneratePresignedUrlRequest(bucket, fileName)
						.withMethod(HttpMethod.PUT)
						.withExpiration(getPreSignedUrlExpiration());
		generatePresignedUrlRequest.addRequestParameter(
				Headers.S3_CANNED_ACL,
				CannedAccessControlList.PublicRead.toString());
		return generatePresignedUrlRequest;
	}

	/**
	 * presigned url 유효 기간 설정
	 * @return 유효기간
	 */
	private Date getPreSignedUrlExpiration() {
		Date expiration = new Date();
		long expTimeMillis = expiration.getTime();
		expTimeMillis += 1000 * 60 * 2;
		expiration.setTime(expTimeMillis);
		return expiration;
	}

	/**
	 * 파일 고유 ID를 생성
	 * @return 36자리의 UUID
	 */
	private String createFileId() {
		return UUID.randomUUID().toString();
	}

	/**
	 * 파일의 전체 경로를 생성
	 * @return 파일의 전체 경로
	 */
	public String createPath(String fileName) {
		String fileId = createFileId();
		return String.format("IMAGE/%s", fileId + "_" + fileName);
	}
}