package com.github.ku4marez.payment.dto;

import com.github.ku4marez.payment.entity.RefundStatus;

import java.math.BigDecimal;
import java.time.Instant;

public record RefundResponse(
    String id,
    String paymentId,
    RefundStatus status,
    String providerRefundRef,
    BigDecimal amount,
    String currency,
    Instant creationDate,
    Instant updatedDate
) {}
