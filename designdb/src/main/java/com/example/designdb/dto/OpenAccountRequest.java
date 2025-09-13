package com.example.designdb.dto;

import com.example.designdb.domain.common.AccountType;

import jakarta.validation.constraints.NotNull;

public record OpenAccountRequest(
        @NotNull Long customerId,
        @NotNull AccountType accountType,
        String accountNo  // 선택
) {}
