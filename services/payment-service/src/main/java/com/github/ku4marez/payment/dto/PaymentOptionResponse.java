package com.github.ku4marez.payment.dto;

import com.github.ku4marez.payment.entity.PaymentProvider;
import com.github.ku4marez.payment.entity.PaymentStatus;

public record PaymentOptionResponse(
    String id,
    String orderId,
    PaymentStatus status,
    PaymentProvider provider
) {}
