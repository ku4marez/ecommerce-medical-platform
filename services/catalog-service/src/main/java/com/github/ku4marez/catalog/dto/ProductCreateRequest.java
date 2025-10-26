package com.github.ku4marez.catalog.dto;

import com.github.ku4marez.catalog.entity.ProductStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.util.Map;

public record ProductCreateRequest(
    @NotBlank String sku,
    @NotBlank String slug,
    @NotBlank String name,
    String description,
    @NotNull ProductStatus status,
    @NotNull @Positive BigDecimal price,
    @NotBlank String currency,
    String categoryId,
    Map<String, Object> attributes
) {}
