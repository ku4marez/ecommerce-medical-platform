package com.github.ku4marez.inventory.entity;

import com.github.ku4marez.commonlibraries.entity.common.PersistentAuditedEntity;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter @Setter
@EqualsAndHashCode(callSuper = true)
@Document(collection = "stock_items")
@CompoundIndexes({
    @CompoundIndex(name = "ix_stock_product", def = "{'productId': 1}", unique = true)
})
public class StockItemEntity extends PersistentAuditedEntity {
    private String productId;
    private Integer available; // on-hand minus reserved
    private Integer reserved;  // currently reserved
}
