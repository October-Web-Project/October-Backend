package com.october.back.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum JwtErrorCode {
    INVALID_JWT_TOKEN(401, "Invalid JWT token"),
    JWT_TOKEN_EXPIRED(401, "JWT token has expired"),
    UNSUPPORTED_JWT_TOKEN(401, "JWT token is unsupported"),
    EMPTY_JWT_CLAIMS(401, "JWT claims string is empty"),
    JWT_SIGNATURE_MISMATCH(401, "JWT signature does not match"),
    JWT_COMPLEX_ERROR(401, "JWT Complex error");

    private final int code;
    private final String message;
}