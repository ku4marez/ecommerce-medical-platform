package com.github.ku4marez.catalog.repository;

import com.github.ku4marez.catalog.entity.ProductEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductRepository extends MongoRepository<ProductEntity, String> {
    Optional<ProductEntity> findBySlug(String slug);
    Optional<ProductEntity> findBySku(String sku);

    @Query("{ $text: { $search: ?0 } }")
    Page<ProductEntity> textSearch(String query, Pageable pageable);
}
