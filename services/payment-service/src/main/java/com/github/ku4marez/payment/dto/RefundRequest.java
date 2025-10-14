package com.github.ku4marez.payment.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record RefundRequest(
    @NotBlank String paymentId,
    @NotNull @Positive BigDecimal amount,
    String reason
) {}
