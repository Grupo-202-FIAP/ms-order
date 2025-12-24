package com.nextime.order.application.mapper;

import com.nextime.order.infrastructure.controller.dto.request.OrderRequest;
import com.nextime.order.infrastructure.controller.dto.response.OrderResponse;
import com.nextime.order.infrastructure.persistence.document.Order;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    Order toDomain(OrderRequest request);

    OrderResponse toResponse(Order order);
}
