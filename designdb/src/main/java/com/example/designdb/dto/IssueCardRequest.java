package com.example.designdb.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.example.designdb.domain.common.CardType;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record IssueCardRequest(
        @NotBlank String cardNo,
        @NotNull Long customerId,
        @NotNull Long accountId,
        @NotNull LocalDate appliedAt,
        BigDecimal creditLimit,
        Integer billingDay,
        @NotNull CardType cardType
) {}
