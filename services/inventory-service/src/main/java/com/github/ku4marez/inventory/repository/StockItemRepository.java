package com.github.ku4marez.inventory.repository;

import com.github.ku4marez.inventory.entity.StockItemEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StockItemRepository extends MongoRepository<StockItemEntity, String> {
    Optional<StockItemEntity> findByProductId(String productId);
}
