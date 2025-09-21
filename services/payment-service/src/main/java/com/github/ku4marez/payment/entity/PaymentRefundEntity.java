package com.github.ku4marez.payment.entity;

import com.github.ku4marez.commonlibraries.entity.common.PersistentAuditedEntity;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@Document(collection = "payment_refunds")
@CompoundIndexes({
    @CompoundIndex(name = "ix_refunds_payment",       def = "{'paymentId': 1}"),
    @CompoundIndex(name = "ix_refunds_provider_ref",  def = "{'providerRefundRef': 1}", unique = true)
})
public class PaymentRefundEntity extends PersistentAuditedEntity {

    private String paymentId;

    private PaymentProvider provider;
    private String providerRefundRef;   // e.g. Stripe refund id

    private RefundStatus status;        // PENDING, SUCCEEDED, FAILED, CANCELED

    private BigDecimal amount;
    private String currency;

    private String reason;              // optional
}
