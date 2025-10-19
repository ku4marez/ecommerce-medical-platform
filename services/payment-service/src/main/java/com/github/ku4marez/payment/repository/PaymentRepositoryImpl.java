package com.github.ku4marez.payment.repository;

import com.github.ku4marez.payment.entity.PaymentEntity;
import com.github.ku4marez.payment.entity.PaymentProvider;
import com.github.ku4marez.payment.entity.PaymentStatus;
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
public class PaymentRepositoryImpl implements PaymentRepositoryCustom {

    private final MongoTemplate mongo;

    @Override
    public Page<PaymentEntity> search(String orderId, PaymentStatus status,
                                      PaymentProvider provider, Instant from,
                                      Instant to, Pageable pageable) {
        Query query = new Query();

        if (StringUtils.hasText(orderId))
            query.addCriteria(Criteria.where("orderId").is(orderId));
        if (status != null)
            query.addCriteria(Criteria.where("status").is(status));
        if (provider != null)
            query.addCriteria(Criteria.where("provider").is(provider));
        if (from != null || to != null) {
            var dateCrit = new Criteria("creationDate");
            if (from != null && to != null) dateCrit.gte(from).lte(to);
            else if (from != null) dateCrit.gte(from);
            else dateCrit.lte(to);
            query.addCriteria(dateCrit);
        }

        long total = mongo.count(query, PaymentEntity.class);
        query.with(pageable);
        List<PaymentEntity> list = mongo.find(query, PaymentEntity.class);
        return new PageImpl<>(list, pageable, total);
    }

    @Override
    public List<PaymentEntity> findOptions(String search, PaymentStatus status, int limit) {
        Query query = new Query();

        if (status != null)
            query.addCriteria(Criteria.where("status").is(status));
        if (StringUtils.hasText(search)) {
            query.addCriteria(new Criteria().orOperator(
                Criteria.where("id").regex(search, "i"),
                Criteria.where("orderId").regex(search, "i")
            ));
        }

        query.limit(limit);
        query.fields().include("id").include("orderId").include("status").include("provider");
        return mongo.find(query, PaymentEntity.class);
    }
}
