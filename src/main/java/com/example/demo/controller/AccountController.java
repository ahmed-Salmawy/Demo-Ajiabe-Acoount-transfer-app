package com.example.demo.controller;


import com.example.demo.controller.dto.AccountRequestDto;
import com.example.demo.controller.dto.AccountResponse;
import com.example.demo.service.AccountService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/va/account")
public class AccountController {

    private final AccountService service;

    public AccountController(AccountService service) {
        this.service = service;
    }

    @PostMapping
    public AccountResponse createAccount(@RequestBody @Valid AccountRequestDto payload) {
        return service.createAccount(payload);
    }


}
