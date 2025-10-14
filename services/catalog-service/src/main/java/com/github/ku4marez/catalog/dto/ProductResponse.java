package com.github.ku4marez.catalog.dto;

import com.github.ku4marez.catalog.entity.ProductStatus;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Map;

public record ProductResponse(
    String id, String sku, String slug, String name, String description,
    ProductStatus status, BigDecimal price, String currency, String categoryId,
    Map<String, Object> attributes, List<String> imageIds,
    Instant creationDate, Instant updatedDate
) {}
