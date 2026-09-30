package com.example.demo.exception;

import com.example.demo.exception.dto.ApiError;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@RestControllerAdvice
public class GlobalExceptionHandler {


    // iam going to handle the exception here in next push


    @ExceptionHandler(CustomGeneralPaymentException.class)
    public ResponseEntity<ApiError> handleCustomGeneralPaymentException(Exception e, HttpServletRequest request) {

        var error = new ApiError(INTERNAL_SERVER_ERROR.value(),
                INTERNAL_SERVER_ERROR.getReasonPhrase(),
                e.getMessage(),
                request.getRequestURI(),
                Instant.now());

        return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(error);

    }

    @ExceptionHandler(CustomPaymentException.class)
    public ResponseEntity<ApiError> handleCustomPaymentException(CustomPaymentException e, HttpServletRequest request) {

        var error = new ApiError(
                e.getCode().httpStatus.value(),
                e.getCode().errorCode,
                e.getMessage(),
                request.getRequestURI(),
                Instant.now());

        return ResponseEntity.status(e.getCode().httpStatus).body(error);

    }
}
