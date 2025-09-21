package com.github.ku4marez.inventory.entity;

public enum ReservationStatus {
    PENDING,   // created, awaiting payment/confirmation
    CONFIRMED, // payment succeeded; will be consumed / fulfilled
    RELEASED,  // explicitly released (cancel/timeout handled)
    EXPIRED    // auto-removed by TTL; treat like released in consumers
}
