package com.example.demo.service;


import com.example.demo.controller.dto.TransferRequest;
import com.example.demo.controller.dto.TransferResponse;
import com.example.demo.dto.TransferStatusEnum;
import com.example.demo.exception.CustomBadRequestException;
import com.example.demo.service.model.Account;
import com.example.demo.service.model.Transaction;
import com.example.demo.utility.TransactionSequenceGenerator;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
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
        //validate accounts exists;
        var fromAccount = accountService.getAccountById(request.fromAccount());
        var toAccount = accountService.getAccountById(request.toAccount());
        if (toAccount.isEmpty()) {
            throw new CustomBadRequestException("to Account is not valid");

        }
        if (fromAccount.isEmpty()) {
            throw new CustomBadRequestException("from account is not valid");
        }
        if (request.amount().compareTo(fromAccount.map(Account::getBalance).orElse(BigDecimal.ZERO)) > 0) {
            throw new CustomBadRequestException("Balance is not sufficient ");
        }
        var transaction = Transaction.builder()
                .id(sequenceGenerator.next())
                .fromAccount(request.fromAccount())
                .toAccount(request.toAccount())
                .amount(request.amount())
                .status(TransferStatusEnum.PENDING)
                .build();
        accountService.deposit(request.fromAccount(),request.amount());
        accountService.deduct(request.toAccount(),request.amount());
        transactions.put(transaction.getId(), transaction);


        return TransferResponse.builder().id(transaction.getId())
                .fromAccount(transaction.getFromAccount())
                .toAccount(transaction.getToAccount())
                .amount(transaction.getAmount())
                .staus(transaction.getStatus())
                .build();

    }

}
