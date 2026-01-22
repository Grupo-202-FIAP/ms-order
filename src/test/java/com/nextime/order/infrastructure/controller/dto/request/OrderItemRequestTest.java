package com.nextime.order.infrastructure.controller.dto.request;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

class OrderItemRequestTest {

    @Test
    void shouldCreateOrderItemRequest() {
        // Given
        Long productId = 1L;
        Integer quantity = 2;
        BigDecimal priceAtPurchase = BigDecimal.valueOf(25.50);

        // When
        OrderItemRequest request = new OrderItemRequest(productId, quantity, priceAtPurchase);

        // Then
        assertThat(request.productId()).isEqualTo(productId);
        assertThat(request.quantity()).isEqualTo(quantity);
        assertThat(request.priceAtPurchase()).isEqualTo(priceAtPurchase);
    }

    @Test
    void shouldCreateOrderItemRequestWithBuilder() {
        // Given
        Long productId = 1L;
        Integer quantity = 2;
        BigDecimal priceAtPurchase = BigDecimal.valueOf(25.50);

        // When
        OrderItemRequest request = OrderItemRequest.builder()
                .productId(productId)
                .quantity(quantity)
                .priceAtPurchase(priceAtPurchase)
                .build();

        // Then
        assertThat(request.productId()).isEqualTo(productId);
        assertThat(request.quantity()).isEqualTo(quantity);
        assertThat(request.priceAtPurchase()).isEqualTo(priceAtPurchase);
    }
}

