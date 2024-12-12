package com.october.back.global.exception;

import com.october.back.global.common.BusinessException;
import com.october.back.global.common.ErrorCode;
import lombok.Getter;

@Getter
public class MediaException extends BusinessException {

	public MediaException(ErrorCode errorCode) {
		super(errorCode);
	}
}
