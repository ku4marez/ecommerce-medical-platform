package com.github.ku4marez.catalog.entity;

import com.github.ku4marez.commonlibraries.entity.common.PersistentAuditedEntity;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@Document(collection = "product_images")
@CompoundIndexes({
    @CompoundIndex(name = "ix_product_images_product", def = "{'productId': 1, 'sort': 1}")
})
public class ProductImageEntity extends PersistentAuditedEntity {
    private String productId;
    private String s3Key;
    private String mimeType;
    private Integer width;
    private Integer height;
    private Integer sort;    // ordering
}
