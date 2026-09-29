package com.example.demo.controller.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record TransferRequest(
        @NotBlank(message = "from account id is required")
        String fromAccount,
        @NotBlank(message = "to account id is required")
        String toAccount,
        @NotNull(message = "amount is required")
        @Min(value = 1L, message = "amount is required")
        BigDecimal amount) {
}
