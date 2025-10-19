package com.github.ku4marez.inventory.repository;

import com.github.ku4marez.inventory.entity.ReservationEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface ReservationRepository extends MongoRepository<ReservationEntity, String>,
    ReservationRepositoryCustom {
    Optional<ReservationEntity> findByProductIdAndOrderId(String productId, String orderId);
}
