package com.github.ku4marez.order.dto;

import com.github.ku4marez.order.entity.OrderStatus;

import java.math.BigDecimal;
import java.util.List;

public record OrderResponse(
    String id,
    String customerId,
    OrderStatus status,
    BigDecimal totalAmount,
    String currency,
    String idempotencyKey,
    List<OrderItemResponse> items,
    String paymentLinkId,
    java.time.Instant creationDate,
    java.time.Instant updatedDate
) {}
