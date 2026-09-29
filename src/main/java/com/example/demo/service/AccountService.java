package com.example.demo.service;

import com.example.demo.controller.dto.AccountRequestDto;
import com.example.demo.controller.dto.AccountResponse;
import com.example.demo.service.model.Account;
import com.example.demo.utility.AccountIdGeneratorUtility;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class AccountService {

    private final Set<Account> accounts;
    private final AccountIdGeneratorUtility accountIdGeneratorUtility;

    public AccountService(AccountIdGeneratorUtility accountIdGeneratorUtility) {
        this.accountIdGeneratorUtility = accountIdGeneratorUtility;
        this.accounts = new HashSet<>();
    }

    public AccountResponse createAccount(AccountRequestDto requestDto) {
        var account = Account.builder()
                .accountId(accountIdGeneratorUtility.getNextAccountId())
                .customerId(requestDto.customerId())
                .balance(requestDto.balance())
                .build();

        accounts.add(account);

        AccountResponse.builder()
                .accountId(account.getAccountId())
                .customerId(account.getCustomerId())
                .balance(account.getBalance())
                .build();

    }

}
