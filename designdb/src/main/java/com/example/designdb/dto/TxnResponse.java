package com.example.designdb.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.example.designdb.domain.common.TxnType;

public record TxnResponse(
        Long id,
        Integer txnNo,
        Long accountId,
        LocalDateTime txnAt,
        TxnType txnType,
        String description,
        BigDecimal amount,
        BigDecimal balanceAfter
) {}
