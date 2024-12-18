package com.october.back.media.entity;

import com.october.back.global.common.BaseEntity;
import com.october.back.review.entity.Review;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "MEDIA")
public class Media extends BaseEntity {

	@Column(name = "clientUploadFileName", nullable = false)
	private String clientUploadFileName;

	@Column(name = "serverStoreUrl", nullable = false, unique = true)
	private String serverStoredUrl;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "review_id")
	private Review review;

	@Builder
	private Media(String clientUploadFileName, String serverStoredUrl) {
		this.clientUploadFileName = clientUploadFileName;
		this.serverStoredUrl = serverStoredUrl;
	}

	@Builder
	private Media(String clientUploadFileName, String serverStoredUrl, Review review) {
		this.clientUploadFileName = clientUploadFileName;
		this.serverStoredUrl = serverStoredUrl;
		this.review = review;
	}

	public void addReview(Review review) {
		if (!review.getMediaList().contains(this)) {
			this.review = review;
			review.getMediaList().add(this);
		}
	}
}