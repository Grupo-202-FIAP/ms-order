package com.nextime.order.infrastructure.controller.dto.response;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class OrderItemResponseTest {

    @Test
    void shouldCreateOrderItemResponse() {
        // Given
        UUID id = UUID.randomUUID();
        ProductResponse product = ProductResponse.builder()
                .id(1L)
                .name("Product")
                .unitPrice(BigDecimal.valueOf(10.0))
                .build();
        int quantity = 2;
        BigDecimal priceAtPurchase = BigDecimal.valueOf(20.0);

        // When
        OrderItemResponse response = new OrderItemResponse(id, product, quantity, priceAtPurchase);

        // Then
        assertThat(response.id()).isEqualTo(id);
        assertThat(response.product()).isEqualTo(product);
        assertThat(response.quantity()).isEqualTo(quantity);
        assertThat(response.priceAtPurchase()).isEqualTo(priceAtPurchase);
    }

    @Test
    void shouldCreateOrderItemResponseWithBuilder() {
        // Given
        UUID id = UUID.randomUUID();
        ProductResponse product = ProductResponse.builder()
                .id(1L)
                .name("Product")
                .unitPrice(BigDecimal.valueOf(10.0))
                .build();
        int quantity = 2;
        BigDecimal priceAtPurchase = BigDecimal.valueOf(20.0);

        // When
        OrderItemResponse response = OrderItemResponse.builder()
                .id(id)
                .product(product)
                .quantity(quantity)
                .priceAtPurchase(priceAtPurchase)
                .build();

        // Then
        assertThat(response.id()).isEqualTo(id);
        assertThat(response.product()).isEqualTo(product);
        assertThat(response.quantity()).isEqualTo(quantity);
        assertThat(response.priceAtPurchase()).isEqualTo(priceAtPurchase);
    }
}

