package com.nextime.order.infrastructure.persistence.document;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class OrderItemTest {

    @Test
    void shouldCreateOrderItemWithBuilder() {
        // Given
        UUID id = UUID.randomUUID();
        Product product = Product.builder()
                .id(1L)
                .name("Product")
                .unitPrice(BigDecimal.valueOf(10.0))
                .build();
        Integer quantity = 2;

        // When
        OrderItem orderItem = OrderItem.builder()
                .id(id)
                .product(product)
                .quantity(quantity)
                .build();

        // Then
        assertThat(orderItem.getId()).isEqualTo(id);
        assertThat(orderItem.getProduct()).isEqualTo(product);
        assertThat(orderItem.getQuantity()).isEqualTo(quantity);
    }

    @Test
    void shouldCreateOrderItemWithNoArgsConstructor() {
        // When
        OrderItem orderItem = new OrderItem();

        // Then
        assertThat(orderItem).isNotNull();
    }

    @Test
    void shouldCreateOrderItemWithAllArgsConstructor() {
        // Given
        UUID id = UUID.randomUUID();
        Product product = Product.builder()
                .id(1L)
                .name("Product")
                .unitPrice(BigDecimal.valueOf(10.0))
                .build();
        Integer quantity = 2;

        // When
        OrderItem orderItem = new OrderItem(id, product, quantity);

        // Then
        assertThat(orderItem.getId()).isEqualTo(id);
        assertThat(orderItem.getProduct()).isEqualTo(product);
        assertThat(orderItem.getQuantity()).isEqualTo(quantity);
    }

    @Test
    void shouldSetAndGetProperties() {
        // Given
        OrderItem orderItem = new OrderItem();
        UUID id = UUID.randomUUID();
        Product product = Product.builder()
                .id(1L)
                .name("Product")
                .unitPrice(BigDecimal.valueOf(10.0))
                .build();
        Integer quantity = 2;

        // When
        orderItem.setId(id);
        orderItem.setProduct(product);
        orderItem.setQuantity(quantity);

        // Then
        assertThat(orderItem.getId()).isEqualTo(id);
        assertThat(orderItem.getProduct()).isEqualTo(product);
        assertThat(orderItem.getQuantity()).isEqualTo(quantity);
    }
}

