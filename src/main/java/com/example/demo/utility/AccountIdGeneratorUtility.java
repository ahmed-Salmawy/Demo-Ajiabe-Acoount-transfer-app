package com.example.demo.utility;

import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicLong;

@Component
public class AccountIdGeneratorUtility {

    private final AtomicLong seq = new AtomicLong(0);


    public String getNextAccountId() {
        return "ACC-%06d".formatted(seq.incrementAndGet());
    }

}
