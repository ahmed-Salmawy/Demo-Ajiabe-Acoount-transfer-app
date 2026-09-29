package com.example.demo.controller.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record AccountRequestDto(
        @NotBlank(message = "customer id not valid ")
        String customerId,
        @NotNull @Size(min = 1000, message = "balance must be greater than 1000 ")
        BigDecimal balance) {

}
