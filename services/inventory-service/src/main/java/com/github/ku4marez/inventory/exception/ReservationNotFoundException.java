package com.github.ku4marez.inventory.exception;

import com.github.ku4marez.ecom.starters.web.exception.NotFoundException;

public class ReservationNotFoundException extends NotFoundException {
    public ReservationNotFoundException(String productId, String orderId) {
        super("reservation not found for productId: " + productId + ", orderId: " + orderId);
    }
}
