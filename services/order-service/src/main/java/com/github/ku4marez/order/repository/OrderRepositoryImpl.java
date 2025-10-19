package com.github.ku4marez.order.repository;

import com.github.ku4marez.order.entity.OrderEntity;
import com.github.ku4marez.order.entity.OrderStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.time.Instant;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class OrderRepositoryImpl implements OrderRepositoryCustom {

    private final MongoTemplate mongo;

    @Override
    public Page<OrderEntity> search(String customerId, OrderStatus status,
                                    Instant from, Instant to, Pageable pageable) {
        Query query = new Query();

        if (StringUtils.hasText(customerId))
            query.addCriteria(Criteria.where("customerId").is(customerId));

        if (status != null)
            query.addCriteria(Criteria.where("status").is(status));

        if (from != null || to != null) {
            var dateCrit = new Criteria("creationDate");
            if (from != null && to != null) dateCrit.gte(from).lte(to);
            else if (from != null) dateCrit.gte(from);
            else dateCrit.lte(to);
            query.addCriteria(dateCrit);
        }

        long total = mongo.count(query, OrderEntity.class);
        query.with(pageable);
        List<OrderEntity> results = mongo.find(query, OrderEntity.class);
        return new PageImpl<>(results, pageable, total);
    }

    @Override
    public List<OrderEntity> findOptions(String search, OrderStatus status, int limit) {
        Query query = new Query();

        if (status != null)
            query.addCriteria(Criteria.where("status").is(status));

        if (StringUtils.hasText(search)) {
            query.addCriteria(new Criteria().orOperator(
                Criteria.where("id").regex(search, "i"),
                Criteria.where("customerId").regex(search, "i")
            ));
        }

        query.limit(limit);
        query.fields().include("id").include("status").include("totalAmount").include("customerId");

        return mongo.find(query, OrderEntity.class);
    }
}
