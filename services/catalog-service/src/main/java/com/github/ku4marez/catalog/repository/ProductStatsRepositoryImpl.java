package com.github.ku4marez.catalog.repository;

import com.github.ku4marez.catalog.entity.ProductStatsEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import java.time.Instant;

@Repository
@RequiredArgsConstructor
class ProductStatsRepositoryImpl implements ProductStatsRepositoryCustom {

    private final MongoTemplate mongo;

    @Override
    public void upsertCounters(String productId, long qty, Instant when) {
        var q = new Query(Criteria.where("productId").is(productId));
        var u = new Update()
            .setOnInsert("productId", productId)
            .inc("ordersCount", 1)
            .inc("unitsSold", Math.max(0, qty))
            .max("lastOrderedAt", when);

        mongo.upsert(q, u, ProductStatsEntity.class);
        // alternatively:
        // mongo.update(ProductStatsEntity.class).matching(q).apply(u).upsert();
    }
}

