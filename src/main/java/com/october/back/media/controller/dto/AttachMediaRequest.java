package com.october.back.media.controller.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class AttachMediaRequest {

	private List<String> mediaFileNames;
}
