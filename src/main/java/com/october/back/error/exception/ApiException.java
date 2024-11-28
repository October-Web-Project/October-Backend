package com.october.back.error.exception;

import com.october.back.error.ErrorCode;
import lombok.Getter;


@Getter
public abstract class ApiException extends RuntimeException {
    private final ErrorCode errorCode;

    public ApiException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}