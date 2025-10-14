package com.github.ku4marez.catalog.dto;

import com.github.ku4marez.catalog.entity.ProductStatus;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.apache.commons.digester.annotations.rules.BeanPropertySetter;

import java.math.BigDecimal;
import java.util.Map;

public record ProductUpdateRequest(
    @NotBlank String name,
    String description,
    @NotNull ProductStatus status,
    @NotNull @DecimalMin("0.0") BigDecimal price,
    @NotBlank String currency,
    String categoryId,
    Map<String, Object> attributes,
    BeanPropertySetter.List<String> imageIds
) {}
