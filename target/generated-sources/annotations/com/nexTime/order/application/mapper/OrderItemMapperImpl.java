package com.nextime.order.application.mapper;

import com.nextime.order.domain.enums.Category;
import com.nextime.order.infrastructure.controller.dto.request.OrderItemRequest;
import com.nextime.order.infrastructure.controller.dto.response.OrderItemResponse;
import com.nextime.order.infrastructure.controller.dto.response.ProductResponse;
import com.nextime.order.infrastructure.persistence.document.OrderItem;
import com.nextime.order.infrastructure.persistence.document.Product;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-12-23T21:09:03-0300",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.44.0.v20251118-1623, environment: Java 21.0.9 (Eclipse Adoptium)"
)
@Component
public class OrderItemMapperImpl implements OrderItemMapper {

    @Override
    public OrderItem toDomain(OrderItemRequest request) {
        if ( request == null ) {
            return null;
        }

        OrderItem orderItem = new OrderItem();

        orderItem.setQuantity( request.quantity() );

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

        id = orderItem.getId();
        product = productToProductResponse( orderItem.getProduct() );
        if ( orderItem.getQuantity() != null ) {
            quantity = orderItem.getQuantity();
        }

        BigDecimal priceAtPurchase = null;

        OrderItemResponse orderItemResponse = new OrderItemResponse( id, product, quantity, priceAtPurchase );

        return orderItemResponse;
    }

    protected ProductResponse productToProductResponse(Product product) {
        if ( product == null ) {
            return null;
        }

        Long id = null;
        String name = null;
        BigDecimal unitPrice = null;

        id = product.getId();
        name = product.getName();
        unitPrice = product.getUnitPrice();

        Category category = null;
        LocalDateTime createdAt = null;
        LocalDateTime updatedAt = null;

        ProductResponse productResponse = new ProductResponse( id, name, category, unitPrice, createdAt, updatedAt );

        return productResponse;
    }
}
