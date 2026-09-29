package com.example.demo.utility;

import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicLong;

@Component
public class TransactionSequenceGenerator {
    private final AtomicLong seq = new AtomicLong(0);

    public String next() {

        return "TRX-%10d".formatted(seq.incrementAndGet());

    }

}
