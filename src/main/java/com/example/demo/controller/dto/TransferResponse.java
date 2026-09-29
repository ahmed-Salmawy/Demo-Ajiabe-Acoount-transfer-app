package com.example.demo.controller.dto;

import com.example.demo.dto.TransferStatusEnum;
import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record TransferResponse(
        String id,
        String fromAccount,
        String toAccount,
        BigDecimal amount,
        TransferStatusEnum staus
) {
}
