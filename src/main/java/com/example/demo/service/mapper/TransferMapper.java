package com.example.demo.service.mapper;

import com.example.demo.controller.dto.TransferResponse;
import com.example.demo.service.model.Transaction;
import org.springframework.stereotype.Component;


public class TransferMapper {


    public static TransferResponse mapToTransferResponse(Transaction transaction) {
        return TransferResponse.builder().id(transaction.getId())
                .fromAccount(transaction.getFromAccount())
                .toAccount(transaction.getToAccount())
                .amount(transaction.getAmount())
                .staus(transaction.getStatus())
                .build();

    }
}