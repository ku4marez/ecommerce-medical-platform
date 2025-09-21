package com.github.ku4marez.payment.entity;

import com.github.ku4marez.commonlibraries.entity.common.PersistentAuditedEntity;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter @Setter
@EqualsAndHashCode(callSuper = true)
@Document(collection = "payment_links")
@CompoundIndexes({
    @CompoundIndex(name = "ix_payment_links_order", def = "{'orderId': 1}"),
    @CompoundIndex(name = "ix_payment_links_provider_ref", def = "{'provider': 1, 'providerRef': 1}", unique = true)
})
public class PaymentEntity extends PersistentAuditedEntity {
    private String orderId;
    private PaymentProvider provider;      // e.g. "STRIPE"
    private String providerRef;   // e.g. PaymentIntent/CheckoutSession id
    private PaymentStatus status; // PENDING, SUCCEEDED, FAILED
    private String checkoutUrl;   // client-facing url
}
