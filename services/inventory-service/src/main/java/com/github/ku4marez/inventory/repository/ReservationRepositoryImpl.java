package com.github.ku4marez.inventory.repository;

import com.github.ku4marez.inventory.entity.ReservationEntity;
import com.github.ku4marez.inventory.entity.ReservationStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
class ReservationRepositoryImpl implements ReservationRepositoryCustom {
    private final MongoTemplate mongo;

    @Override
    public Page<ReservationEntity> search(String productId, String orderId, ReservationStatus status, Pageable pageable) {
        Query q = new Query();
        if (productId != null) q.addCriteria(Criteria.where("productId").is(productId));
        if (orderId != null) q.addCriteria(Criteria.where("orderId").is(orderId));
        if (status != null) q.addCriteria(Criteria.where("status").is(status));

        long total = mongo.count(q, ReservationEntity.class);
        q.with(pageable);
        List<ReservationEntity> list = mongo.find(q, ReservationEntity.class);
        return new PageImpl<>(list, pageable, total);
    }
}
