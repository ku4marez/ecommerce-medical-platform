package com.github.ku4marez.order.dto;

import com.github.ku4marez.order.entity.OrderStatus;

import java.math.BigDecimal;

public record OrderOptionResponse(
    String id,
    String customerId,
    OrderStatus status,
    BigDecimal totalAmount
) {}
