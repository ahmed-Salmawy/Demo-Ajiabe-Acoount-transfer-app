package com.example.demo.controller.dto;


import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record AccountRequestDto(
        @NotBlank(message = "customer id not valid ")
        String customerId,
        @NotNull @Min(value = 1000, message = "balance must be greater than 1000 ")
        BigDecimal balance) {

}
