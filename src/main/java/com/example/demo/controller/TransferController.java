package com.example.demo.controller;

import com.example.demo.controller.dto.TransferRequest;
import com.example.demo.controller.dto.TransferResponse;
import com.example.demo.service.TransferService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/transfer")
public class TransferController {

    private final TransferService service;

    public TransferController(TransferService service) {
        this.service = service;
    }


    @PostMapping
    public TransferResponse transfer(@RequestBody @Valid TransferRequest payload) {
        return service.transfer(payload);
    }

}
