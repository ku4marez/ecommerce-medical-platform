package com.github.ku4marez.catalog.dto;

import com.github.ku4marez.catalog.entity.ProductStatus;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.Map;

public record ProductCreateRequest(
    @NotBlank String sku,
    @NotBlank String slug,
    @NotBlank String name,
    String description,
    @NotNull ProductStatus status,
    @NotNull @DecimalMin("0.0") BigDecimal price,
    @NotBlank String currency,
    String categoryId,
    Map<String, Object> attributes
) {}
