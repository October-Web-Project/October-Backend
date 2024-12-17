package com.october.back.review.service;

import com.october.back.global.exception.ReviewException;
import com.october.back.global.exception.UserException;
import com.october.back.media.controller.dto.AttachMediaRequest;
import com.october.back.media.entity.Media;
import com.october.back.media.service.MediaService;
import com.october.back.review.entity.Review;
import com.october.back.review.repository.ReviewRepository;
import com.october.back.user.entity.Users;
import com.october.back.user.repository.UserRepository;
import com.october.back.review.service.dto.ReviewServiceDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.october.back.global.common.ErrorCode.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReviewService {

	private final ReviewRepository reviewRepository;
	private final UserRepository userRepository;
	private final MediaService mediaService;

	@Transactional
	public Review createReview(ReviewServiceDto request) {
		Users user = userRepository.findById(request.getUserId())
				.orElseThrow(() -> new UserException(NOT_FOUND_USER));

		Review review = request.toEntity(user);
		return reviewRepository.save(review);
	}

	@Transactional
	public void attachMediaToReview(Long reviewId, AttachMediaRequest request) {
		Review review = reviewRepository.findById(reviewId)
				.orElseThrow(() -> new ReviewException(NOT_FOUND_REVIEW));

		for (String originalFileName : request.getMediaFileNames()) {

			String serverStoredUrl = mediaService.createPath(originalFileName);

			Media media = Media.builder()
					.clientUploadFileName(originalFileName)
					.serverStoredUrl(serverStoredUrl)
					.build();
			media.addReview(review);
		}
	}
}
