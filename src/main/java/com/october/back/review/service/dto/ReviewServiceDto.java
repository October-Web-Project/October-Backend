package com.october.back.review.service.dto;

import com.october.back.media.entity.Media;
import com.october.back.review.entity.Review;
import com.october.back.user.entity.Users;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class ReviewServiceDto {
	private Long id;
	private String title;
	private String content;
	private Double rating;
	private Integer views;
	private LocalDateTime createdAt;
	private Long userId;
	private List<String> mediaFileNames;
	@Builder
	private ReviewServiceDto(Long id, String title, String content, Double rating,
							 Integer views, LocalDateTime createdAt, List<String> mediaFileNames,
							 Long userId) {
		this.id = id;
		this.title = title;
		this.content = content;
		this.rating = rating;
		this.views = views;
		this.createdAt = createdAt;
		this.userId = userId;
		this.mediaFileNames = mediaFileNames;
	}
	public Review toEntity(Users user) {
		return Review.builder()
				.title(this.title)
				.content(this.content)
				.rating(this.rating)
				.views(this.views)
				.user(user)
				.build();
	}
	public static ReviewServiceDto from(Review review) {
		return ReviewServiceDto.builder()
				.id(review.getId())
				.title(review.getTitle())
				.content(review.getContent())
				.rating(review.getRating())
				.views(review.getViews())
				.createdAt(review.getCreatedAt())
				.userId(review.getUser().getId())
				.mediaFileNames(review.getMediaList().stream()
						.map(Media::getClientUploadFileName)
						.collect(Collectors.toList()))
				.build();
	}
}