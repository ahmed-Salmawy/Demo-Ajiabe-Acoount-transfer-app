package com.example.demo.service.model;


import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

@Data
@Builder
public class Account {
    private String accountId;
    private String customerId;
    @EqualsAndHashCode.Exclude
    private BigDecimal balance;
    //created at


}
