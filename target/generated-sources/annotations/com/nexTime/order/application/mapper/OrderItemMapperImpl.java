package com.nexTime.order.application.mapper;

import com.nexTime.order.infrastructure.controller.dto.request.OrderItemRequest;
import com.nexTime.order.infrastructure.controller.dto.response.OrderItemResponse;
import com.nexTime.order.infrastructure.controller.dto.response.ProductResponse;
import com.nexTime.order.infrastructure.persistence.document.OrderItem;
import com.nexTime.order.infrastructure.persistence.document.Product;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-12-23T01:49:19-0300",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.12 (Oracle Corporation)"
)
@Component
public class OrderItemMapperImpl implements OrderItemMapper {

    @Override
    public OrderItem toDomain(OrderItemRequest request) {
        if ( request == null ) {
            return null;
        }

        OrderItem.OrderItemBuilder orderItem = OrderItem.builder();

        orderItem.quantity( request.quantity() );

        return orderItem.build();
    }

    @Override
    public OrderItemResponse toResponse(OrderItem orderItem) {
        if ( orderItem == null ) {
            return null;
        }

        OrderItemResponse.OrderItemResponseBuilder orderItemResponse = OrderItemResponse.builder();

        orderItemResponse.id( orderItem.getId() );
        orderItemResponse.product( productToProductResponse( orderItem.getProduct() ) );
        if ( orderItem.getQuantity() != null ) {
            orderItemResponse.quantity( orderItem.getQuantity() );
        }

        return orderItemResponse.build();
    }

    protected ProductResponse productToProductResponse(Product product) {
        if ( product == null ) {
            return null;
        }

        ProductResponse.ProductResponseBuilder productResponse = ProductResponse.builder();

        productResponse.id( product.getId() );
        productResponse.name( product.getName() );
        productResponse.unitPrice( product.getUnitPrice() );

        return productResponse.build();
    }
}
