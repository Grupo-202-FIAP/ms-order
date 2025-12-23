package com.nextime.order.application.mapper;

import com.nextime.order.infrastructure.controller.dto.request.OrderItemRequest;
import com.nextime.order.infrastructure.controller.dto.response.OrderItemResponse;
import com.nextime.order.infrastructure.persistence.document.OrderItem;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OrderItemMapper {

    OrderItem toDomain(OrderItemRequest request);

    OrderItemResponse toResponse(OrderItem orderItem);
}
