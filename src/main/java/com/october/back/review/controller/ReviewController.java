package com.october.back.review.controller;

import com.october.back.media.controller.dto.AttachMediaRequest;
import com.october.back.review.controller.dto.ReviewRequest;
import com.october.back.review.controller.dto.ReviewResponse;
import com.october.back.review.entity.Review;
import com.october.back.review.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/reviews")
@RequiredArgsConstructor
public class ReviewController {

	private final ReviewService reviewService;

	@PostMapping("")
	public ResponseEntity<ReviewResponse> createReview(@RequestBody ReviewRequest request) {
		Review review = reviewService.createReview(request.toServiceDto());
		return ResponseEntity.ok().body(ReviewResponse.from(review));
	}

	@PostMapping("/{reviewId}/media")
	public ResponseEntity<ReviewResponse> attachMediaToReview(@PathVariable(name = "reviewId") Long reviewId, @RequestBody AttachMediaRequest request) {
		reviewService.attachMediaToReview(reviewId, request);
		return ResponseEntity.ok().build();
	}
}