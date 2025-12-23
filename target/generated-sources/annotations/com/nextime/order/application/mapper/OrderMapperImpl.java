package com.nextime.order.application.mapper;

import com.nextime.order.infrastructure.controller.dto.request.OrderItemRequest;
import com.nextime.order.infrastructure.controller.dto.request.OrderRequest;
import com.nextime.order.infrastructure.controller.dto.response.OrderItemResponse;
import com.nextime.order.infrastructure.controller.dto.response.OrderResponse;
import com.nextime.order.infrastructure.controller.dto.response.ProductResponse;
import com.nextime.order.infrastructure.persistence.document.Order;
import com.nextime.order.infrastructure.persistence.document.OrderItem;
import com.nextime.order.infrastructure.persistence.document.Product;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-12-23T20:21:38-0300",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.12 (Oracle Corporation)"
)
@Component
public class OrderMapperImpl implements OrderMapper {

    @Override
    public Order toDomain(OrderRequest request) {
        if ( request == null ) {
            return null;
        }

        Order.OrderBuilder order = Order.builder();

        order.customerId( request.customerId() );
        order.items( orderItemRequestListToOrderItemList( request.items() ) );

        return order.build();
    }

    @Override
    public OrderResponse toResponse(Order order) {
        if ( order == null ) {
            return null;
        }

        OrderResponse.OrderResponseBuilder orderResponse = OrderResponse.builder();

        orderResponse.id( order.getId() );
        orderResponse.identifier( order.getIdentifier() );
        orderResponse.totalPrice( order.getTotalPrice() );
        orderResponse.status( order.getStatus() );
        orderResponse.createdAt( order.getCreatedAt() );
        orderResponse.items( orderItemListToOrderItemResponseList( order.getItems() ) );

        return orderResponse.build();
    }

    protected OrderItem orderItemRequestToOrderItem(OrderItemRequest orderItemRequest) {
        if ( orderItemRequest == null ) {
            return null;
        }

        OrderItem.OrderItemBuilder orderItem = OrderItem.builder();

        orderItem.quantity( orderItemRequest.quantity() );

        return orderItem.build();
    }

    protected List<OrderItem> orderItemRequestListToOrderItemList(List<OrderItemRequest> list) {
        if ( list == null ) {
            return null;
        }

        List<OrderItem> list1 = new ArrayList<OrderItem>( list.size() );
        for ( OrderItemRequest orderItemRequest : list ) {
            list1.add( orderItemRequestToOrderItem( orderItemRequest ) );
        }

        return list1;
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

    protected OrderItemResponse orderItemToOrderItemResponse(OrderItem orderItem) {
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

    protected List<OrderItemResponse> orderItemListToOrderItemResponseList(List<OrderItem> list) {
        if ( list == null ) {
            return null;
        }

        List<OrderItemResponse> list1 = new ArrayList<OrderItemResponse>( list.size() );
        for ( OrderItem orderItem : list ) {
            list1.add( orderItemToOrderItemResponse( orderItem ) );
        }

        return list1;
    }
}
