package com.example.demo.controller;

import com.example.demo.controller.dto.TransferRequest;
import com.example.demo.controller.dto.TransferResponse;
import com.example.demo.service.TransferService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/transfer")
public class TransferController {

    private final TransferService service;

    public TransferController(TransferService service) {
        this.service = service;
    }


    @PostMapping
    public TransferResponse transfer(@RequestBody @Valid TransferRequest payload, @RequestHeader("Idempotency-Key") String idempotencyKey) {

        return service.transfer(payload);

    }

    @GetMapping("/{transferId}")
    public TransferResponse getTransferById(@NotNull @PathVariable("transferId") String transferId) {

        return service.getTransferById(transferId);

    }

}
