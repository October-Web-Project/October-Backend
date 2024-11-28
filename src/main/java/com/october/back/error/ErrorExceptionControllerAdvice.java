package com.october.back.error;

import com.october.back.error.exception.*;

import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RequiredArgsConstructor
@RestControllerAdvice
public class ErrorExceptionControllerAdvice {

    private ResponseEntity<ErrorEntity> buildResponseEntity(ApiException e) {
        return ResponseEntity
                .status(e.getErrorCode().getStatus())
                .body(ErrorEntity.builder()
                        .errorCode(e.getErrorCode().getCode())
                        .errorMessage(e.getMessage())
                        .build());
    }

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ErrorEntity> handleApiException(ApiException e) {
        return buildResponseEntity(e);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorEntity> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ErrorEntity.builder()
                        .errorCode("400")
                        .errorMessage(e.getBindingResult().getAllErrors().get(0).getDefaultMessage())
                        .build());
    }


    @ExceptionHandler(BindException.class)
    public ResponseEntity<ErrorEntity> handleBindException(BindException e) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ErrorEntity.builder()
                        .errorCode(ErrorCode.PARAMETER_VALID_EXCEPTION.getCode())
                        .errorMessage(e.getBindingResult().getAllErrors().get(0).getDefaultMessage())
                        .build());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorEntity> handleGeneralException(Exception e) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ErrorEntity.builder()
                        .errorCode("500")
                        .errorMessage("Unexpected error occurred: " + e.getMessage())
                        .build());
    }



}
