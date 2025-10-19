package com.github.ku4marez.catalog.service;

@Getter @Setter
@Document("product_stats")
@CompoundIndexes({
    @CompoundIndex(name="ix_product_stats_product", def="{'productId':1}", unique = true)
})
public class ProductStatsEntity {
    @Id private String id;
    private String productId;
    private long ordersCount;
    private long unitsSold;
    private Instant lastOrderedAt;
}

