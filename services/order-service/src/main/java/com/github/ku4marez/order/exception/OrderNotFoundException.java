package com.github.ku4marez.order.exception;

import com.github.ku4marez.ecom.starters.web.exception.NotFoundException;

public class OrderNotFoundException extends NotFoundException {
    public OrderNotFoundException(String orderId) {
        super("Order with id " + orderId + " not found");
    }
}
