package com.github.ku4marez.order.configuration;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class OrderSagaConsumer {

    @KafkaListener(topics = "${ecom.kafka.stock-topic}", groupId = "orders-service")
    public void onStockEvent(String message) {
        // advance/cancel order based on stock reservation results
    }

    @KafkaListener(topics = "${ecom.kafka.payment-topic}", groupId = "orders-service")
    public void onPaymentEvent(String message) {
        // advance order based on payment success/failure
    }
}
