package com.october.back.error.exception;

import com.october.back.error.ErrorCode;

public class InternalServerException extends BusinessException {

    public InternalServerException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }
}
