package com.github.ku4marez.payment.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record CreatePaymentRequest(
    @NotBlank String orderId,
    @NotNull @Positive BigDecimal amount,
    @NotBlank String currency   // "usd", "eur", ...
) {}
