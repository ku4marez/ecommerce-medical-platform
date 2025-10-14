package com.github.ku4marez.inventory.repository;

import com.github.ku4marez.inventory.entity.ReservationEntity;
import com.github.ku4marez.inventory.entity.ReservationStatus;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReservationRepository extends MongoRepository<ReservationEntity, String> {
    Optional<ReservationEntity> findByProductIdAndOrderId(String productId, String orderId);
    List<ReservationEntity> findByProductIdAndStatus(String productId, ReservationStatus status);
}
