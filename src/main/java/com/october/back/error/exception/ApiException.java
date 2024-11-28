package com.october.back.error.exception;

import com.october.back.error.ErrorCode;
import org.springframework.http.HttpStatus;

public abstract class ApiException extends RuntimeException {
    private final ErrorCode errorCode;

    public ApiException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public HttpStatus getStatus() {
        return errorCode.getStatus();
    }

    public ErrorCode getErrorCode() { // getErrorCode 메서드 정의
        return errorCode;
    }

    public String getCode() {
        return errorCode.getCode();
    }

    @Override
    public String getMessage() {
        return errorCode.getMessage();
    }
}
