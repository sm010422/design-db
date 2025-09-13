package com.example.designdb.dto;

import jakarta.validation.constraints.*;

public record CustomerCreateRequest(
        @NotBlank String rrn,
        @NotBlank String name,
        String address,
        @Past @NotNull java.time.LocalDate birthDate,
        @Email String email,
        String phone,
        String job
) {}
