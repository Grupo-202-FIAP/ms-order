package com.nextime.order.infrastructure.controller.dto.response;

import com.nextime.order.domain.enums.OrderStatus;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class OrderResponseTest {

    @Test
    void shouldCreateOrderResponse() {
        // Given
        UUID id = UUID.randomUUID();
        String identifier = "ORD-1234";
        BigDecimal totalPrice = BigDecimal.valueOf(100.50);
        OrderStatus status = OrderStatus.RECEIVED;
        LocalDateTime createdAt = LocalDateTime.now();
        List<OrderItemResponse> items = new ArrayList<>();

        // When
        OrderResponse response = new OrderResponse(id, identifier, totalPrice, status, createdAt, items);

        // Then
        assertThat(response.id()).isEqualTo(id);
        assertThat(response.identifier()).isEqualTo(identifier);
        assertThat(response.totalPrice()).isEqualTo(totalPrice);
        assertThat(response.status()).isEqualTo(status);
        assertThat(response.createdAt()).isEqualTo(createdAt);
        assertThat(response.items()).isEqualTo(items);
    }

    @Test
    void shouldCreateOrderResponseWithBuilder() {
        // Given
        UUID id = UUID.randomUUID();
        String identifier = "ORD-1234";
        BigDecimal totalPrice = BigDecimal.valueOf(100.50);
        OrderStatus status = OrderStatus.RECEIVED;
        LocalDateTime createdAt = LocalDateTime.now();
        List<OrderItemResponse> items = new ArrayList<>();

        // When
        OrderResponse response = OrderResponse.builder()
                .id(id)
                .identifier(identifier)
                .totalPrice(totalPrice)
                .status(status)
                .createdAt(createdAt)
                .items(items)
                .build();

        // Then
        assertThat(response.id()).isEqualTo(id);
        assertThat(response.identifier()).isEqualTo(identifier);
        assertThat(response.totalPrice()).isEqualTo(totalPrice);
        assertThat(response.status()).isEqualTo(status);
        assertThat(response.createdAt()).isEqualTo(createdAt);
        assertThat(response.items()).isEqualTo(items);
    }
}

