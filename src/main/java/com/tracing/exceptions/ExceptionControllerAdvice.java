package com.tracing.exceptions;

import com.tracing.api.exceptions.ErrorResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Date;

@ControllerAdvice
public class ExceptionControllerAdvice {

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(Exception e) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        return new ResponseEntity<>(
            ErrorResponse.builder()
                .status(HttpStatus.BAD_REQUEST)
                .code(400)
                .message(e.getMessage())
                .timestamp(new Date())
                .build(),
            status
        );
    }
}
