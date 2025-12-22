package com.nexTime.order.application.mapper;

import com.nexTime.order.infrastructure.controller.dto.request.OrderItemRequest;
import com.nexTime.order.infrastructure.persistence.document.OrderItem;
import com.nexTime.order.infrastructure.persistence.document.Product;
import org.springframework.stereotype.Component;

@Component
public class OrdemItemMapper {
    public OrderItem toDomain(OrderItemRequest request) {
        return OrderItem.builder()
                .product(
                        Product.builder()
                                .id(request.productId())
                                .build()
                )
                .quantity(request.quantity())
                .build();
    }
}
