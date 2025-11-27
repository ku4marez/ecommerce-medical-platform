package com.github.ku4marez.payment.dto;

public record ExternalPaymentResponse(
    String providerRef,
    String checkoutUrl
) {}
