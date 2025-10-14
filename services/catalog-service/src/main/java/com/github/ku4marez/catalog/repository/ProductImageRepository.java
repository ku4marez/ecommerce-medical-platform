package com.github.ku4marez.catalog.repository;

import com.github.ku4marez.catalog.entity.ProductImageEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductImageRepository extends MongoRepository<ProductImageEntity, String> {
    List<ProductImageEntity> findByProductIdOrderBySortAsc(String productId);
}
