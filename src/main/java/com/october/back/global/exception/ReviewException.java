package com.october.back.global.exception;

import com.october.back.global.common.BusinessException;
import com.october.back.global.common.ErrorCode;

public class ReviewException extends BusinessException {

	public ReviewException(ErrorCode errorCode) {
		super(errorCode);
	}
}
