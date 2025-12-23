package com.nexTime.order.application.mapper;

import com.nexTime.order.infrastructure.controller.dto.request.OrderItemRequest;
import com.nexTime.order.infrastructure.controller.dto.response.OrderItemResponse;
import com.nexTime.order.infrastructure.persistence.document.OrderItem;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OrderItemMapper {

    OrderItem toDomain(OrderItemRequest request);

    OrderItemResponse toResponse(OrderItem orderItem);
}
