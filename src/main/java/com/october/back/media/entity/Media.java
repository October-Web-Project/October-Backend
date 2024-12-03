package com.october.back.media.entity;

import com.october.back.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Media extends BaseEntity {

	@Column(name = "clientUploadImageUrl", nullable = false)
	private String clientUploadUrl;

	@Column(name = "serverStoreImageUrl", nullable = false, unique = true)
	private String serverStoredUrl;

	@Embedded
	@Enumerated(EnumType.STRING)
	private MediaType mediaType;
}
