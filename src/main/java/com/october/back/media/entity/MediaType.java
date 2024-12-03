package com.october.back.media.entity;

import jakarta.persistence.Embeddable;
import lombok.Getter;

@Getter
@Embeddable
public enum MediaType {

	IMAGE, VIDEO
}
