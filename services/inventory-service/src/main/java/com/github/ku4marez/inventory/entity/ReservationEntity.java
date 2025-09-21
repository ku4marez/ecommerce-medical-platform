package com.github.ku4marez.inventory.entity;

import com.github.ku4marez.commonlibraries.entity.common.PersistentAuditedEntity;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Getter @Setter
@EqualsAndHashCode(callSuper = true)
@Document(collection = "reservations")
@CompoundIndexes({
    @CompoundIndex(name = "ix_reservation_product_order", def = "{'productId': 1, 'orderId': 1}", unique = true),
    @CompoundIndex(name = "ix_reservation_status", def = "{'status': 1}"),
    // TTL index added by initializer on 'expiresAt'
})
public class ReservationEntity extends PersistentAuditedEntity {
    private String productId;
    private String orderId;
    private Integer quantity;
    private ReservationStatus status;   // PENDING, CONFIRMED, RELEASED, EXPIRED
    private Instant expiresAt;          // TTL target
}
