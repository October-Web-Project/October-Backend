package com.october.back.review.controller.dto;

import com.october.back.review.service.dto.ReviewServiceDto;
import lombok.*;

@Getter
@Setter
public class ReviewRequest {
	private String title;
	private String content;
	private Double rating;
	private Long userId;
	@Builder
	private ReviewRequest(String title, String content, Double rating, Long userId) {
		this.title = title;
		this.content = content;
		this.rating = rating;
		this.userId = userId;
	}
	public ReviewServiceDto toServiceDto() {
		return ReviewServiceDto.builder()
				.userId(this.userId)
				.title(this.title)
				.content(this.content)
				.rating(this.rating)
				.views(0)
				.build();
	}
}