package com.october.back.error.exception;

import com.october.back.error.ErrorCode;

public class ServerException extends ApiException {
    public ServerException(ErrorCode errorCode) {
        super(errorCode);
    }

    public ServerException(String message, ErrorCode errorCode) {
        super(errorCode);
    }
}
