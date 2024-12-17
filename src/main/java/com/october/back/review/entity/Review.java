package com.october.back.review.entity;

import com.october.back.global.common.BaseEntity;
import com.october.back.media.entity.Media;
import com.october.back.user.entity.Users;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AttributeOverride(name = "id", column = @Column(name = "review_id"))
public class Review extends BaseEntity {

	@Column(name = "title", nullable = false)
	private String title;

	@Column(name = "content", nullable = false)
	private String content;

	@Column(name = "rating", nullable = false)
	@ColumnDefault("1")
	private Double rating;

	@Column(name = "views")
	@ColumnDefault("0")
	private Integer views;

	@ManyToOne(fetch = FetchType.LAZY)
	private Users user;

	@OneToMany(mappedBy = "review", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Media> mediaList = new ArrayList<>();

	@Builder
	private Review(String title, String content, Double rating, Integer views, Users user) {
		this.title = title;
		this.content = content;
		this.rating = rating;
		this.views = views;
		this.user = user;
		this.mediaList = new ArrayList<>();
	}

	public void addMedia(Media media) {

		if (media.getReview() != this) {
			this.mediaList.add(media);
			media.addReview(this);
		}
	}
}
