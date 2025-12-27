package com.nextime.order.domain;

import com.nextime.order.infrastructure.persistence.document.OrderItem;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;

class OrderRequestTest {

    @Test
    void shouldCreateOrderRequestWithNoArgsConstructor() {
        // When
        OrderRequest orderRequest = new OrderRequest();

        // Then
        assertThat(orderRequest.getOrderItems()).isNull();
    }

    @Test
    void shouldCreateOrderRequestWithAllArgsConstructor() {
        // Given
        List<OrderItem> items = new ArrayList<>();
        items.add(new OrderItem());

        // When
        OrderRequest orderRequest = new OrderRequest(items);

        // Then
        assertThat(orderRequest.getOrderItems()).isEqualTo(items);
        assertThat(orderRequest.getOrderItems()).hasSize(1);
    }

    @Test
    void shouldSetAndGetOrderItems() {
        // Given
        OrderRequest orderRequest = new OrderRequest();
        List<OrderItem> items = new ArrayList<>();
        items.add(new OrderItem());

        // When
        orderRequest.setOrderItems(items);

        // Then
        assertThat(orderRequest.getOrderItems()).isEqualTo(items);
    }
}

