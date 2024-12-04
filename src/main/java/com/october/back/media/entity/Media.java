package com.october.back.media.entity;

import com.october.back.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Media extends BaseEntity {

	@Column(name = "clientUploadFileName", nullable = false)
	private String clientUploadFileName;

	@Column(name = "serverStoreUrl", nullable = false, unique = true)
	private String serverStoredUrl;

	@Enumerated(EnumType.STRING)
	private MediaType mediaType;

	@Builder
	private Media(String clientUploadFileName, String serverStoredUrl, MediaType mediaType) {
		this.clientUploadFileName = clientUploadFileName;
		this.serverStoredUrl = serverStoredUrl;
		this.mediaType = mediaType;
	}
}
