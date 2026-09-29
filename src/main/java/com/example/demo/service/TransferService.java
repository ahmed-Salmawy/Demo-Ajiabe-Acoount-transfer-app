package com.example.demo.service;


import com.example.demo.controller.dto.TransferRequest;
import com.example.demo.controller.dto.TransferResponse;
import com.example.demo.dto.TransferStatusEnum;
import com.example.demo.exception.CustomBadRequestException;
import com.example.demo.exception.CustomGeneralPaymentException;
import com.example.demo.service.model.Transaction;
import com.example.demo.utility.TransactionSequenceGenerator;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class TransferService {
    private final Map<String, Transaction> transactions;
    private final AccountService accountService;
    private final TransactionSequenceGenerator sequenceGenerator;

    public TransferService(Map<String, Transaction> transactions, AccountService accountService, TransactionSequenceGenerator sequenceGenerator) {
        this.transactions = transactions;
        this.accountService = accountService;
        this.sequenceGenerator = sequenceGenerator;
    }


    public TransferResponse transfer(@Valid TransferRequest request) {

        Transaction transaction = Transaction.builder()
                .id(sequenceGenerator.next())
                .fromAccount(request.fromAccount())
                .toAccount(request.toAccount())
                .amount(request.amount())
                .status(TransferStatusEnum.PENDING)
                .build();

        try {
            accountService.transfer(request);
            transaction.setStatus(TransferStatusEnum.COMPETED);
        } catch (CustomBadRequestException exception) {
            transaction.setStatus(TransferStatusEnum.FAILED);
            throw exception;
        } catch (Exception e) {
            transaction.setStatus(TransferStatusEnum.FAILED);
            throw new CustomGeneralPaymentException(e.getMessage());

        } finally {
            transactions.put(transaction.getId(), transaction);

        }

        return TransferResponse.builder().id(transaction.getId())
                .fromAccount(transaction.getFromAccount())
                .toAccount(transaction.getToAccount())
                .amount(transaction.getAmount())
                .staus(transaction.getStatus())
                .build();

    }

}
