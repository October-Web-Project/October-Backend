package com.october.back.global.common;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public abstract class BusinessException extends RuntimeException {

  private final ErrorCode errorCode;
  private List<ErrorResponse.FieldError> errors = new ArrayList<>();

  protected BusinessException(String message, ErrorCode errorCode) {
    super(message);
    this.errorCode = errorCode;
  }

  protected BusinessException(ErrorCode errorCode) {
    this.errorCode = errorCode;
  }

  protected BusinessException(ErrorCode errorCode, List<ErrorResponse.FieldError> errors) {
    super(errorCode.getMessage());
    this.errorCode = errorCode;
    this.errors = errors;
  }
}
