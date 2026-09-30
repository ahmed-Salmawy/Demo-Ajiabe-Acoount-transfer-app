package com.example.demo.service;


import com.example.demo.controller.dto.TransferRequest;
import com.example.demo.controller.dto.TransferResponse;
import com.example.demo.dto.TransferStatusEnum;
import com.example.demo.exception.CustomBadRequestException;
import com.example.demo.exception.CustomGeneralPaymentException;
import com.example.demo.exception.CustomPaymentException;
import com.example.demo.service.mapper.TransferMapper;
import com.example.demo.service.model.Transaction;
import com.example.demo.utility.TransactionSequenceGenerator;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class TransferService {
    private final Map<String, Transaction> transactions;
    private final AccountService accountService;
    private final TransactionSequenceGenerator sequenceGenerator;
    private final TransferMapper transferMapper;

    public TransferService(AccountService accountService, TransactionSequenceGenerator sequenceGenerator, TransferMapper transferMapper) {
        this.transferMapper = transferMapper;
        this.transactions = new ConcurrentHashMap<>();
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
        } catch (Exception e) {
            transaction.setStatus(TransferStatusEnum.FAILED);
            if (e instanceof CustomBadRequestException customBadRequestException) {
                throw customBadRequestException;
            }
            throw new CustomGeneralPaymentException(e.getMessage());

        } finally {
            transactions.put(transaction.getId(), transaction);
        }

        return TransferMapper.mapToTransferResponse(transaction);

    }


    public TransferResponse getTransferById(@NotNull String transferId) {

        return Optional.ofNullable(this.transactions.get(transferId))
                .map(TransferMapper::mapToTransferResponse)
                .orElseThrow(() -> new CustomPaymentException(CustomPaymentException.PaymentErrorCode.TRANSFER_NOT_FOUND, "transfer of id %s not found".formatted(transferId)));
    }
}
