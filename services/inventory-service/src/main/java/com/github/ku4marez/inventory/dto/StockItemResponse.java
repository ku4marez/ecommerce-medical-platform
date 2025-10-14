package com.github.ku4marez.inventory.dto;

import java.time.Instant;

public record StockItemResponse(
    String productId,
    Integer available,
    Integer reserved,
    Instant creationDate,
    Instant updatedDate
) {}
