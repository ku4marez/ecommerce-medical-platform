package com.github.ku4marez.inventory.dto;

import jakarta.validation.constraints.NotBlank;

// Mark reservation as CONFIRMED (e.g., after payment.succeeded)
public record ConfirmRequest(
    @NotBlank String productId,
    @NotBlank String orderId
) {}
