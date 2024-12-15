package com.october.back.review.controller.dto;

import com.october.back.review.entity.Review;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ReviewResponse {
	private Long id;
	private String title;
	private String content;
	private Double rating;
	private Integer views;
	private boolean isUpdated;
	private Long userId;

	public static ReviewResponse from(Review review) {
		return ReviewResponse.builder()
				.id(review.getId())
				.title(review.getTitle())
				.content(review.getContent())
				.rating(review.getRating())
				.views(review.getViews())
				.userId(review.getUser().getId())
				.build();
	}
}