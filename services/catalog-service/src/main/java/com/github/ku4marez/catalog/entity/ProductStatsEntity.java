package com.github.ku4marez.catalog.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Getter
@Setter
@Document("product_stats")
@CompoundIndexes({
    @CompoundIndex(name="ix_product_stats_product", def="{'productId':1}", unique = true)
})
public class ProductStatsEntity {
    @Id
    private String id;
    private String productId;
    private long ordersCount;
    private long unitsSold;
    private Instant lastOrderedAt;
}

