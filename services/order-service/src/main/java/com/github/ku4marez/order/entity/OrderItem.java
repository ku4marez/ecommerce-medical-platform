package com.github.ku4marez.order.entity;

import com.github.ku4marez.commonlibraries.entity.common.PersistentAuditedEntity;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;

@Getter @Setter
public class OrderItem {
    private String productId;
    private String productName;   // denormalized for history
    private String sku;
    private Integer quantity;
    private BigDecimal unitPrice;
}
