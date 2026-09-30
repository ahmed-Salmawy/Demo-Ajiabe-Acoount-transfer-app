package com.example.demo.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class CustomPaymentException extends RuntimeException {

    private final PaymentErrorCode code;

    public enum PaymentErrorCode {
        TRANSFER_NOT_FOUND("TRANSFER_NOT_FOUND", HttpStatus.NOT_FOUND);
        final HttpStatus httpStatus;
        final String errorCode;

        PaymentErrorCode(String errorCode, HttpStatus status) {
            this.httpStatus = status;
            this.errorCode = errorCode;
        }
    }


    public CustomPaymentException(PaymentErrorCode errorCode, String message) {
        super(message);
        this.code = errorCode;
    }
}
