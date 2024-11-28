package com.october.back.error.exception;

import com.october.back.error.ErrorCode;

public class UnAuthorizedException extends ApiException {
    public UnAuthorizedException(ErrorCode errorCode) {
        super(errorCode);
    }

    public UnAuthorizedException(String message, ErrorCode errorCode) {
        super(errorCode);
    }
}
