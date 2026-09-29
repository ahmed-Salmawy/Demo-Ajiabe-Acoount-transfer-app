package com.example.demo.service;

import com.example.demo.controller.dto.AccountRequestDto;
import com.example.demo.controller.dto.AccountResponse;
import com.example.demo.exception.CustomBadRequestException;
import com.example.demo.service.model.Account;
import com.example.demo.utility.AccountIdGeneratorUtility;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
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

    public void deduct(String accountId, BigDecimal amount) {


        if (accounts.get(accountId) == null) {
            throw new CustomBadRequestException("account of id %s not found ".formatted(accountId));
        }

        accounts.computeIfPresent(accountId, (id, account) -> {
            account.setBalance(account.getBalance().subtract(amount));
            return account;
        });


    }

    public void deposit(String accountId, BigDecimal amount) {


        if (accounts.get(accountId) == null) {
            throw new CustomBadRequestException("account of id %s not found ".formatted(accountId));
        }
        accounts.computeIfPresent(accountId, (id, account) -> {
            account.setBalance(account.getBalance().add(amount));
            return account;
        });


    }


}
