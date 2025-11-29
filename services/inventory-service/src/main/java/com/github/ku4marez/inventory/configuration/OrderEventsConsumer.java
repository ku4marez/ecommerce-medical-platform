package com.github.ku4marez.inventory.configuration;

import com.github.ku4marez.inventory.dto.api.ConfirmRequest;
import com.github.ku4marez.inventory.dto.api.ReleaseRequest;
import com.github.ku4marez.inventory.dto.api.ReserveRequest;
import com.github.ku4marez.inventory.service.InventoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class OrderEventsConsumer {
    private final InventoryService inventory;

    @KafkaListener(topics = "${ecom.kafka.order-topic}", groupId = "inventory-service")
    public void onOrderEvent(Map<String, Object> msg) {
        var type = (String) msg.get("eventType");
        var orderId = (String) msg.get("orderId");
        var items = (List<Map<String, Object>>) msg.get("items");

        log.info("Received order event {} for {}", type, orderId);

        switch (type) {
            case "order.created", "order.placed" ->
                items.forEach(i -> inventory.reserve(new ReserveRequest(
                    (String) i.get("productId"),
                    orderId,
                    (Integer) i.get("quantity"),
                    (Integer) msg.get("ttlSeconds")
                )));

            case "order.cancelled", "order.failed" ->
                items.forEach(i -> inventory.release(new ReleaseRequest(
                    (String) i.get("productId"),
                    orderId,
                    "order." + type
                )));

            case "order.confirmed" ->
                items.forEach(i -> inventory.confirm(new ConfirmRequest(
                    (String) i.get("productId"),
                    orderId
                )));

            default -> log.debug("No handler for {}", type);
        }
    }
}


