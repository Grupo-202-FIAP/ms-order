package com.nextime.order.domain.enums;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class OrderStatusTest {

    @Test
    void shouldHaveAllOrderStatuses() {
        // Then
        assertThat(OrderStatus.values()).hasSize(5);
        assertThat(OrderStatus.RECEIVED).isNotNull();
        assertThat(OrderStatus.PREPARING).isNotNull();
        assertThat(OrderStatus.READY).isNotNull();
        assertThat(OrderStatus.COMPLETED).isNotNull();
        assertThat(OrderStatus.CANCELLED).isNotNull();
    }

    @Test
    void shouldReturnCorrectStatusString() {
        // Then
        assertThat(OrderStatus.RECEIVED.getStatus()).isEqualTo("RECEIVED");
        assertThat(OrderStatus.PREPARING.getStatus()).isEqualTo("PREPARING");
        assertThat(OrderStatus.READY.getStatus()).isEqualTo("READY");
        assertThat(OrderStatus.COMPLETED.getStatus()).isEqualTo("COMPLETED");
        assertThat(OrderStatus.CANCELLED.getStatus()).isEqualTo("CANCELLED");
    }

    @Test
    void shouldHaveCorrectEnumValues() {
        // Then
        assertThat(OrderStatus.valueOf("RECEIVED")).isEqualTo(OrderStatus.RECEIVED);
        assertThat(OrderStatus.valueOf("PREPARING")).isEqualTo(OrderStatus.PREPARING);
        assertThat(OrderStatus.valueOf("READY")).isEqualTo(OrderStatus.READY);
        assertThat(OrderStatus.valueOf("COMPLETED")).isEqualTo(OrderStatus.COMPLETED);
        assertThat(OrderStatus.valueOf("CANCELLED")).isEqualTo(OrderStatus.CANCELLED);
    }
}

