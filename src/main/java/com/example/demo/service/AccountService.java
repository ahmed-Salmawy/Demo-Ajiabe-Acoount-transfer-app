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

@Service
public class AccountService {

    private final Map<String, Account> accounts;
    private final AccountIdGeneratorUtility accountIdGeneratorUtility;

    public AccountService(AccountIdGeneratorUtility accountIdGeneratorUtility) {
        this.accountIdGeneratorUtility = accountIdGeneratorUtility;
        this.accounts = new ConcurrentHashMap<>();
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


    public Optional<Account> getAccountById(String accountId) {
        return Optional.ofNullable(accounts.get(accountId));
    }


    public void transfer(@Valid TransferRequest request) {

        if (accounts.get(request.fromAccount()) == null) {
            throw new CustomBadRequestException("from account of id %s not found ".formatted(request.fromAccount()));
        }

        if (accounts.get(request.toAccount()) == null) {
            throw new CustomBadRequestException("to account of id %s not found ".formatted(request.toAccount()));
        }
        var fromAccount = accounts.get(request.fromAccount());

        if (request.amount().compareTo(fromAccount.getBalance()) > 0) {
            throw new CustomBadRequestException("Balance is not sufficient ");
        }

        accounts.computeIfPresent(request.fromAccount(), (id, account) -> {
            account.setBalance(account.getBalance().subtract(request.amount()));
            return account;
        });
        accounts.computeIfPresent(request.toAccount(), (id, account) -> {
            account.setBalance(account.getBalance().add(request.amount()));
            return account;
        });


    }


}
