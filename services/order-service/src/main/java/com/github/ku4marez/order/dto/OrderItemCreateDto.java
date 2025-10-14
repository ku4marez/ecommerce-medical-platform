package com.github.ku4marez.order.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record OrderItemCreateDto(
    @NotBlank String productId,
    @NotBlank String productName,      // denormalized (from Catalog)
    @NotBlank String sku,
    @NotNull @Positive Integer quantity,
    @NotNull @Positive BigDecimal unitPrice
) {
}
