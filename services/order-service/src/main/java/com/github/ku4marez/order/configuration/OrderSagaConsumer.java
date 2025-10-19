package com.github.ku4marez.order.configuration;

import com.github.ku4marez.order.entity.OrderStatus;
import com.github.ku4marez.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class OrderSagaConsumer {
    private final OrderRepository repo;
    private final OrderEventsPublisher publisher;

    @KafkaListener(topics = "${ecom.kafka.stock-topic}", groupId = "orders-service")
    public void onStockEvent(Map<String, Object> msg) {
        var type = (String) msg.get("eventType");
        var orderId = (String) msg.get("orderId");
        log.info("Received stock event {} for {}", type, orderId);

        switch (type) {
            case "stock.reserved" -> handleStockReserved(orderId);
            case "stock.released" -> handleStockReleased(orderId);
            case "stock.confirmed" -> handleStockConfirmed(orderId);
            default -> log.debug("Unhandled stock event {}", type);
        }
    }

    @KafkaListener(topics = "${ecom.kafka.payment-topic}", groupId = "orders-service")
    public void onPaymentEvent(Map<String, Object> msg) {
        var type = (String) msg.get("eventType");
        var orderId = (String) msg.get("orderId");
        log.info("Received payment event {} for {}", type, orderId);

        switch (type) {
            case "payment.succeeded" -> handlePaymentSucceeded(orderId);
            case "payment.failed" -> handlePaymentFailed(orderId);
            default -> log.debug("Unhandled payment event {}", type);
        }
    }

    private void handleStockReserved(String orderId) {
        var order = repo.findById(orderId).orElseThrow();
        order.setStatus(OrderStatus.RESERVED);
        repo.save(order);
        publisher.publishOrderConfirmed(order);
    }

    private void handleStockReleased(String orderId) {
        var order = repo.findById(orderId).orElseThrow();
        order.setStatus(OrderStatus.CANCELLED);
        repo.save(order);
        publisher.publishOrderCancelled(order, "Stock unavailable");
    }

    private void handleStockConfirmed(String orderId) {
        var order = repo.findById(orderId).orElseThrow();
        order.setStatus(OrderStatus.CONFIRMED);
        repo.save(order);
        publisher.publishOrderConfirmed(order);
    }

    private void handlePaymentSucceeded(String orderId) {
        var order = repo.findById(orderId).orElseThrow();
        order.setStatus(OrderStatus.PAID);
        repo.save(order);
    }

    private void handlePaymentFailed(String orderId) {
        var order = repo.findById(orderId).orElseThrow();
        order.setStatus(OrderStatus.CANCELLED);
        repo.save(order);
        publisher.publishOrderCancelled(order, "Payment failed");
    }
}
