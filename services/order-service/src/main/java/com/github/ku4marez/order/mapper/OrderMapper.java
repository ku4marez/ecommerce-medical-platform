package com.github.ku4marez.order.mapper;

import com.github.ku4marez.order.dto.OrderCreateRequest;
import com.github.ku4marez.order.dto.OrderItemCreateDto;
import com.github.ku4marez.order.dto.OrderItemResponse;
import com.github.ku4marez.order.dto.OrderResponse;
import com.github.ku4marez.order.entity.OrderEntity;
import com.github.ku4marez.order.entity.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@Mapper(
    componentModel = "spring",
    unmappedTargetPolicy = ReportingPolicy.ERROR,
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface OrderMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", constant = "NEW")
    @Mapping(target = "totalAmount", ignore = true)    // compute in service
    @Mapping(target = "idempotencyKey", ignore = true) // set from header
    @Mapping(target = "paymentLinkId", ignore = true)
    @Mapping(target = "items", source = "items")
    @Mapping(target = "creationDate", ignore = true)
    @Mapping(target = "updatedDate", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    OrderEntity toNewEntity(OrderCreateRequest req);

    List<OrderItem> toItems(List<OrderItemCreateDto> items);

    @Mapping(target = "productId",   source = "productId")
    @Mapping(target = "productName", source = "productName")
    @Mapping(target = "sku",         source = "sku")
    @Mapping(target = "quantity",    source = "quantity")
    @Mapping(target = "unitPrice",   source = "unitPrice")
    OrderItem toItem(OrderItemCreateDto dto);

    @Mapping(target = "items", expression = "java(toItemResponses(e.getItems()))")
    OrderResponse toResponse(OrderEntity e);

    default List<OrderItemResponse> toItemResponses(List<OrderItem> list) {
        return list == null ? java.util.List.of()
            : list.stream()
            .map(i -> new OrderItemResponse(
                i.getProductId(), i.getProductName(), i.getSku(),
                i.getQuantity(), i.getUnitPrice()))
            .toList();
    }

    @SuppressWarnings("unused")
    default Instant map(LocalDateTime value) {
        return value != null ? value.atZone(ZoneId.systemDefault()).toInstant() : null;
    }

    @SuppressWarnings("unused")
    default LocalDateTime map(Instant value) {
        return value != null ? LocalDateTime.ofInstant(value, ZoneId.systemDefault()) : null;
    }
}
