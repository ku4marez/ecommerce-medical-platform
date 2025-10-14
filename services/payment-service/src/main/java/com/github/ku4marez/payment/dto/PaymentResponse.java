package com.github.ku4marez.payment.dto;

import com.github.ku4marez.payment.entity.PaymentProvider;
import com.github.ku4marez.payment.entity.PaymentStatus;

import java.time.Instant;

public record PaymentResponse(
    String id,
    String orderId,
    PaymentProvider provider,
    String providerRef,
    PaymentStatus status,
    String checkoutUrl,
    Instant creationDate,
    Instant updatedDate
) {}
