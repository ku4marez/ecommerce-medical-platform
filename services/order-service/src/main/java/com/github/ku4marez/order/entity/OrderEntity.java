package com.github.ku4marez.order.entity;

import com.github.ku4marez.commonlibraries.entity.common.PersistentAuditedEntity;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.util.List;

@Getter @Setter
@EqualsAndHashCode(callSuper = true)
@Document(collection = "orders")
@CompoundIndexes({
    @CompoundIndex(name = "ix_orders_customer_status", def = "{'customerId': 1, 'status': 1}"),
    @CompoundIndex(name = "ix_orders_idempotency", def = "{'idempotencyKey': 1}", unique = true)
})
public class OrderEntity extends PersistentAuditedEntity {
    private String customerId;
    private OrderStatus status;               // NEW, PENDING_PAYMENT, PAID, FULFILLED, FAILED, CANCELLED
    private BigDecimal totalAmount;
    private String currency;
    private String idempotencyKey;            // per-client request key
    private List<OrderItem> items;            // embedded items
    private String paymentLinkId;             // link to PaymentLink
}
