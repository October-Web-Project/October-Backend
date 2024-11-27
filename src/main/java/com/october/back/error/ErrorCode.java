package com.october.back.error;

import lombok.Getter;
import lombok.ToString;
import org.springframework.http.HttpStatus;


@Getter
@ToString
public enum ErrorCode {

    BAD_REQUEST_EXCEPTION(HttpStatus.BAD_REQUEST, "400", "400 Bad Request"),
    PARAMETER_VALID_EXCEPTION(HttpStatus.BAD_REQUEST, "400", "parameter is incorrect"),
    UNAUTHORIZED_EXCEPTION(HttpStatus.UNAUTHORIZED, "401", "Authentication failed"),
    FORBIDDEN_EXCEPTION(HttpStatus.FORBIDDEN, "403", "You do not have permission"),
    NOT_FOUND_EXCEPTION(HttpStatus.NOT_FOUND, "404", "Not found matched data"),
    DUPLICATE_EXCEPTION(HttpStatus.BAD_REQUEST, "400", "cannot Allowed Duplicated "),
    Internal_Error(HttpStatus.INTERNAL_SERVER_ERROR, "500","예상하치 못한 에러, 긴급")

    ;


    private final HttpStatus status;
    private final String code;
    private final String message;

    ErrorCode(HttpStatus status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }
}
