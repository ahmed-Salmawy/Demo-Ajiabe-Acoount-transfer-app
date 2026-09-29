package com.example.demo.service.model;


import com.example.demo.dto.TransferStatusEnum;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class Transaction {
    private String id;
    private String fromAccount;
    private String toAccount;
    private BigDecimal amount;
    private TransferStatusEnum status;
}
