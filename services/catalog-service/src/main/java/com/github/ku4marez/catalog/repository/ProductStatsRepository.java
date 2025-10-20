package com.github.ku4marez.catalog.repository;

import com.github.ku4marez.catalog.entity.ProductStatsEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface ProductStatsRepository
    extends MongoRepository<ProductStatsEntity, String>, ProductStatsRepositoryCustom {

    Optional<ProductStatsEntity> findByProductId(String productId);

    Page<ProductStatsEntity> findAllByOrderByUnitsSoldDesc(Pageable pageable);
}

