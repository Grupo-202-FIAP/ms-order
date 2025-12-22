package com.nexTime.order.application.mapper;

import com.nexTime.order.domain.OrderRequest;
import com.nexTime.order.infrastructure.controller.dto.response.OrderResponse;
import com.nexTime.order.infrastructure.persistence.document.Order;
import org.springframework.stereotype.Component;

@Component
public class OrderMapper {
//    public Order toDomain(OrderRequest request) {
//        return Order.builder()
//                .items(request.getOrderItems()
//                        .stream()
//                        .map(OrdemItemMapper::toDomain)
//                        .toList())
//                .build();
//    }
//
//    public OrderResponse toResponse(Order order) {
//        return OrderResponse.builder()
//                .id(order.getId())
//                .identifier(order.getIdentifier())
//                .status(order.getStatus())
//                .totalPrice(order.getTotalPrice())
//                .orderDateTime(order.getCreatedAt())
//                .items(order.getItems())
//                .items(order.getItens()
//                        .stream()
//                        .map(OrderItemMapper::toResponse)
//                        .toList())
//                .build();
//    }
}
