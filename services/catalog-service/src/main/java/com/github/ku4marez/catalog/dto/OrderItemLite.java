package com.github.ku4marez.catalog.dto;

import java.math.BigDecimal;

public record OrderItemLite(
    String productId,
    int quantity,
    BigDecimal unitPrice
) {}
