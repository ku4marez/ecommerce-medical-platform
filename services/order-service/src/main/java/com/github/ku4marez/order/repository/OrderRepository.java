package com.github.ku4marez.order.repository;

import com.github.ku4marez.order.entity.OrderEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface OrderRepository extends MongoRepository<OrderEntity, String>, OrderRepositoryCustom {
    Optional<OrderEntity> findByIdempotencyKey(String idempotencyKey);
    Page<OrderEntity> findByCustomerIdOrderByCreationDateDesc(String customerId, Pageable pageable);
}
