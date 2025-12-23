package com.nexTime.order.application.mapper;

import com.nexTime.order.domain.enums.OrderStatus;
import com.nexTime.order.infrastructure.controller.dto.request.OrderRequest;
import com.nexTime.order.infrastructure.controller.dto.response.OrderItemResponse;
import com.nexTime.order.infrastructure.controller.dto.response.OrderResponse;
import com.nexTime.order.infrastructure.persistence.document.Order;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-12-23T01:12:15-0300",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.12 (Oracle Corporation)"
)
@Component
public class OrderMapperImpl implements OrderMapper {

    @Override
    public Order toDomain(OrderRequest request) {
        if ( request == null ) {
            return null;
        }

        Order order = new Order();

        return order;
    }

    @Override
    public OrderResponse toResponse(Order order) {
        if ( order == null ) {
            return null;
        }

        UUID id = null;
        String identifier = null;
        BigDecimal totalPrice = null;
        OrderStatus status = null;
        LocalDateTime createdAt = null;
        List<OrderItemResponse> items = null;

        OrderResponse orderResponse = new OrderResponse( id, identifier, totalPrice, status, createdAt, items );

        return orderResponse;
    }
}
