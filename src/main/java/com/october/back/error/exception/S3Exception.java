package com.october.back.error.exception;

import com.october.back.error.ErrorCode;

public class S3Exception extends BusinessException {
    public S3Exception(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }
}
