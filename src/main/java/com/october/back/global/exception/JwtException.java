package com.october.back.global.exception;

import com.october.back.global.common.BusinessException;
import com.october.back.global.common.ErrorCode;

public class JwtException extends BusinessException {
    public JwtException(ErrorCode errorCode) {
        super(errorCode);
    }

    public JwtException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }
}
