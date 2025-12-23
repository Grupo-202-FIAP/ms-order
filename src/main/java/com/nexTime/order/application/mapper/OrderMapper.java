package com.nexTime.order.application.mapper;

import com.nexTime.order.infrastructure.controller.dto.request.OrderRequest;
import com.nexTime.order.infrastructure.controller.dto.response.OrderResponse;
import com.nexTime.order.infrastructure.persistence.document.Order;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = OrderMapper.class)
public interface OrderMapper {

    Order toDomain(OrderRequest request);

    OrderResponse toResponse(Order order);
}