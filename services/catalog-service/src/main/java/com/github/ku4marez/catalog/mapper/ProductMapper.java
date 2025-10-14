package com.github.ku4marez.catalog.mapper;

import com.github.ku4marez.catalog.dto.ProductCreateRequest;
import com.github.ku4marez.catalog.dto.ProductResponse;
import com.github.ku4marez.catalog.dto.ProductUpdateRequest;
import com.github.ku4marez.catalog.entity.ProductEntity;
import org.mapstruct.*;

@Mapper(
    componentModel = "spring",
    unmappedTargetPolicy = ReportingPolicy.ERROR,
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface ProductMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "imageIds", expression = "java(new java.util.ArrayList<>())")
    ProductEntity toNewEntity(ProductCreateRequest req);

    // Partial update onto existing entity
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntity(@MappingTarget ProductEntity entity, ProductUpdateRequest req);

    ProductResponse toResponse(ProductEntity entity);
}
