package com.october.back.error;

import com.october.back.error.exception.*;

import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.http.*;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RequiredArgsConstructor
@RestControllerAdvice
public class ErrorExceptionControllerAdvice {


    @ExceptionHandler({BadRequestException.class})
    public ResponseEntity<ErrorEntity> exceptionhandler(final com.october.back.error.exception.BadRequestException e){
        return ResponseEntity
                .status(e.getErrorCode().getStatus())
                .body(ErrorEntity.builder()
                        .errorCode(e.getErrorCode().getCode())
                        .errorMessage(e.getMessage())
                        .build());
    }

    @ExceptionHandler({BindException.class})
    public ResponseEntity<ErrorEntity> exceptionHandler(final BindException e) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ErrorEntity.builder()
                        .errorCode(ErrorCode.PARAMETER_VALID_EXCEPTION.getCode())
                        .errorMessage(e.getBindingResult().getAllErrors().get(0).getDefaultMessage())
                        .build());
    }

    @ExceptionHandler({UnAuthorizedException.class})
    public ResponseEntity<ErrorEntity> exceptionHandler(final UnAuthorizedException e) {
        return ResponseEntity
                .status(e.getErrorCode().getStatus())
                .body(ErrorEntity.builder()
                        .errorCode(e.getErrorCode().getCode())
                        .errorMessage(e.getMessage())
                        .build());
    }

    @ExceptionHandler({NotFoundException.class})
    public ResponseEntity<ErrorEntity> exceptionHandler(final NotFoundException e) {
        return ResponseEntity
                .status(e.getErrorCode().getStatus())
                .body(ErrorEntity.builder()
                        .errorCode(e.getErrorCode().getCode())
                        .errorMessage(e.getMessage())
                        .build());
    }

    @ExceptionHandler({DuplicateException.class})
    public ResponseEntity<ErrorEntity> exceptionHandler(final DuplicateException e) {
        return ResponseEntity
                .status(e.getErrorCode().getStatus())
                .body(ErrorEntity.builder()
                        .errorCode(e.getErrorCode().getCode())
                        .errorMessage(e.getMessage())
                        .build());
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<ErrorEntity> exceptionHandler(final MethodArgumentNotValidException e) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ErrorEntity.builder()
                        .errorCode("400")
                        .errorMessage(e.getAllErrors().get(0).getDefaultMessage())
                        .build());
    }

    @ExceptionHandler({InternalServerException.class})
    public ResponseEntity<ErrorEntity> exceptionHandler(final InternalServerException e) {

        return ResponseEntity
                .status(e.getErrorCode().getStatus())
                .body(ErrorEntity.builder()
                        .errorCode(e.getErrorCode().getCode())
                        .errorMessage(e.getErrorCode().getMessage())
                        .build());
    }

    @ExceptionHandler({InvalidTokenException.class})
    public ResponseEntity<ErrorEntity> exceptionHandler(final InvalidTokenException e) {
        return ResponseEntity
                .status(e.getErrorCode().getStatus())
                .body(ErrorEntity.builder()
                        .errorCode(e.getErrorCode().getCode())
                        .errorMessage(e.getErrorCode().getMessage())
                        .build());
    }

    @ExceptionHandler({S3Exception.class})
    public ResponseEntity<ErrorEntity> exceptionHandler(final S3Exception e) {
        return ResponseEntity
                .status(e.getErrorCode().getStatus())
                .body(ErrorEntity.builder()
                        .errorCode(e.getErrorCode().getCode())
                        .errorMessage(e.getMessage())
                        .build());
    }
}
