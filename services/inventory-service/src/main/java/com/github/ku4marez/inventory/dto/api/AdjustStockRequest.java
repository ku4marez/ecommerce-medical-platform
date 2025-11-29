package com.github.ku4marez.inventory.dto.api;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

// Adjust on-hand stock (administrative or sync job)
public record AdjustStockRequest(
    @NotBlank String productId,
    @NotNull Integer delta,               // + adds stock, - removes stock
    String reason
) {}
