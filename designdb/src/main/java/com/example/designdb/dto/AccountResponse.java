package com.example.designdb.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.example.designdb.domain.common.AccountType;

public record AccountResponse(
        Long id,
        String accountNo,
        Long customerId,
        AccountType accountType,
        BigDecimal balance,
        boolean cardRequested,
        LocalDate openedAt) {
}
