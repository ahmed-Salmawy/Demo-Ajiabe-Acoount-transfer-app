package com.example.demo.controller.dto;

import lombok.Builder;

import java.math.BigDecimal;
@Builder
public record AccountResponse(
        String accountId, String customerId, BigDecimal balance

) {
}
