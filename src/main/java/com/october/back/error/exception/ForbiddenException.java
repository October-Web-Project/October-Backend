package com.october.back.error.exception;

public class ForbiddenException extends BusinessException {

    public ForbiddenException(String message, com.aders.api.core.error.ErrorCode errorCode) {
        super(message, errorCode);
    }
}
