package com.october.back.global.exception;

import com.october.back.global.common.BusinessException;
import com.october.back.global.common.ErrorCode;
import lombok.Getter;

@Getter
public class UserException extends BusinessException {

  public UserException(ErrorCode errorCode) {
		super(errorCode);
	}
}
