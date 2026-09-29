package com.example.demo.service;

import com.example.demo.controller.dto.AccountRequestDto;
import com.example.demo.controller.dto.AccountResponse;
import com.example.demo.controller.dto.TransferRequest;
import com.example.demo.exception.CustomBadRequestException;
import com.example.demo.service.model.Account;
import com.example.demo.utility.AccountIdGeneratorUtility;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

@Service
public class AccountService {

    private final Map<String, Account> accounts;
    private final AccountIdGeneratorUtility accountIdGeneratorUtility;
    private final Map<String, ReentrantLock> locks;

    public AccountService(AccountIdGeneratorUtility accountIdGeneratorUtility) {
        this.accountIdGeneratorUtility = accountIdGeneratorUtility;
        this.accounts = new ConcurrentHashMap<>();
        locks = new ConcurrentHashMap<>();
    }

    public AccountResponse createAccount(AccountRequestDto requestDto) {
        var account = Account.builder()
                .accountId(accountIdGeneratorUtility.getNextAccountId())
                .customerId(requestDto.customerId())
                .balance(requestDto.balance())
                .build();

        accounts.put(account.getAccountId(), account);

        return AccountResponse.builder()
                .accountId(account.getAccountId())
                .customerId(account.getCustomerId())
                .balance(account.getBalance())
                .build();

    }


    public Account getAccountById(String accountId) {
        return findOrThrow(accountId);
    }

    private ReentrantLock lockFor(String accountId) {
        return locks.computeIfAbsent(accountId, id -> new ReentrantLock());
    }

    public void transfer(@Valid TransferRequest request) {


        var firstId = request.fromAccount().compareTo(request.toAccount()) > 0 ? request.fromAccount() : request.toAccount();
        var secondId = request.fromAccount().compareTo(request.toAccount()) < 0 ? request.toAccount() : request.fromAccount();

        var firstLock = lockFor(firstId);
        var secondLock = lockFor(secondId);

        firstLock.lock();
        try {
            secondLock.lock();
            try {
                var fromAccount = findOrThrow(request.fromAccount());
                var toAccount = findOrThrow(request.toAccount());
                if (request.amount().compareTo(fromAccount.getBalance()) > 0) {

                    throw new CustomBadRequestException("Insufficient balance");

                }
                fromAccount.setBalance(fromAccount.getBalance().subtract(request.amount()));
                toAccount.setBalance(toAccount.getBalance().add(request.amount()));
            } finally {
                secondLock.unlock();
            }
        } finally {
            firstLock.unlock();
        }
    }


    private Account findOrThrow(String accountId) {

        return Optional.ofNullable(accounts.get(accountId)).orElseThrow(() -> new CustomBadRequestException("account of id %s not found ".formatted(accountId)));

    }

}
