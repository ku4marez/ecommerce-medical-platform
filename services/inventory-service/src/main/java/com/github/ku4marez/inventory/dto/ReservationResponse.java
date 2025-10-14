package com.github.ku4marez.inventory.dto;

import com.github.ku4marez.inventory.entity.ReservationStatus;

import java.time.Instant;

public record ReservationResponse(
    String id,
    String productId,
    String orderId,
    Integer quantity,
    ReservationStatus status,
    Instant expiresAt,
    Instant creationDate,
    Instant updatedDate
) {}
