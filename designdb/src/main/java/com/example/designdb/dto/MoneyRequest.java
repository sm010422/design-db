package com.example.designdb.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public record MoneyRequest(
        @NotNull Long accountId,
        @Positive BigDecimal amount,
        String description
) {}
