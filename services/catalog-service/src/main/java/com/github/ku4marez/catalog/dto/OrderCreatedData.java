package com.github.ku4marez.catalog.dto;

import java.util.List;

public record OrderCreatedData(
    String orderId,
    String customerId,
    String currency,
    List<OrderItemLite> items
) {}
