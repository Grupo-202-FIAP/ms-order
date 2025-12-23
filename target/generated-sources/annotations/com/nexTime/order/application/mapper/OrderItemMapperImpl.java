package com.nextime.order.application.mapper;

import com.nextime.order.infrastructure.controller.dto.request.OrderItemRequest;
import com.nextime.order.infrastructure.controller.dto.response.OrderItemResponse;
import com.nextime.order.infrastructure.controller.dto.response.ProductResponse;
import com.nextime.order.infrastructure.persistence.document.OrderItem;
import java.math.BigDecimal;
import java.util.UUID;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-12-23T20:34:42-0300",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.12 (Oracle Corporation)"
)
@Component
public class OrderItemMapperImpl implements OrderItemMapper {

    @Override
    public OrderItem toDomain(OrderItemRequest request) {
        if ( request == null ) {
            return null;
        }

        OrderItem orderItem = new OrderItem();

        return orderItem;
    }

    @Override
    public OrderItemResponse toResponse(OrderItem orderItem) {
        if ( orderItem == null ) {
            return null;
        }

        UUID id = null;
        ProductResponse product = null;
        int quantity = 0;
        BigDecimal priceAtPurchase = null;

        OrderItemResponse orderItemResponse = new OrderItemResponse( id, product, quantity, priceAtPurchase );

        return orderItemResponse;
    }
}
