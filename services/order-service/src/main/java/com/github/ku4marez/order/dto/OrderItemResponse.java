package com.github.ku4marez.order.dto;

import java.math.BigDecimal;

public record OrderItemResponse(
    String productId, String productName, String sku,
    Integer quantity, BigDecimal unitPrice
) {}

