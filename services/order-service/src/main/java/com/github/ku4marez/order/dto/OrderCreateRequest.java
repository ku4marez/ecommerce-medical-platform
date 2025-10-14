package com.github.ku4marez.order.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record OrderCreateRequest(
    @NotBlank String customerId,
    @NotBlank String currency,
    @NotEmpty List<@Valid OrderItemCreateDto> items
) {}
