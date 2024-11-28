package com.october.back.error.exception;

import com.october.back.error.ErrorCode;

public class ClientException extends ApiException {
    public ClientException(ErrorCode errorCode) {
        super(errorCode);
    }
}
