package com.github.ku4marez.inventory.dto.api;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

// Reserve a quantity for an order (idempotent by productId+orderId)
// ttlSeconds -> how long the reservation should live before auto-release via TTL index
public record ReserveRequest(
    @NotBlank String productId,
    @NotBlank String orderId,
    @NotNull @Positive Integer quantity,
    @Positive Integer ttlSeconds
) {}
