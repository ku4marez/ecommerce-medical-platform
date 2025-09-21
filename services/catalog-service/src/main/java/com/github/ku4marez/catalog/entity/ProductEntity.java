package com.github.ku4marez.catalog.entity;

import com.github.ku4marez.commonlibraries.entity.common.PersistentAuditedEntity;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@Document(collection = "products")
@CompoundIndexes({
    @CompoundIndex(name = "ix_products_slug", def = "{'slug': 1}", unique = true),
    @CompoundIndex(name = "ix_products_sku",  def = "{'sku': 1}", unique = true),
    @CompoundIndex(name = "ix_products_status", def = "{'status': 1}")
})
public class ProductEntity extends PersistentAuditedEntity {
    private String sku;                 // unique
    private String slug;                // unique, URL key
    private String name;
    private String description;
    private ProductStatus status;       // DRAFT, ACTIVE, ARCHIVED
    private BigDecimal price;
    private String currency;            // e.g. "USD", "EUR"
    private String categoryId;          // reference to Category
    private Map<String, Object> attributes; // free-form
    private List<String> imageIds;      // ProductImage ids
}
