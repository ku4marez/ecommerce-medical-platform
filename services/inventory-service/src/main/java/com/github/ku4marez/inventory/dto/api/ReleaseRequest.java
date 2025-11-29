package com.github.ku4marez.inventory.dto.api;

import jakarta.validation.constraints.NotBlank;

public record ReleaseRequest(
    @NotBlank String productId,
    @NotBlank String orderId,
    String reason
) {}

