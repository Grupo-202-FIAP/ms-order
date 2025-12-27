package com.nextime.order.domain;

import org.junit.jupiter.api.Test;
import java.util.UUID;
import static org.assertj.core.api.Assertions.assertThat;

class EventFiltersTest {

    @Test
    void shouldReturnTrueWhenOrderIdIsNotNull() {
        // Given
        UUID orderId = UUID.randomUUID();
        EventFilters filters = new EventFilters(orderId, null);

        // When
        boolean result = filters.hasOrderId();

        // Then
        assertThat(result).isTrue();
    }

    @Test
    void shouldReturnFalseWhenOrderIdIsNull() {
        // Given
        EventFilters filters = new EventFilters(null, null);

        // When
        boolean result = filters.hasOrderId();

        // Then
        assertThat(result).isFalse();
    }

    @Test
    void shouldReturnTrueWhenTransactionIdIsNotNull() {
        // Given
        UUID transactionId = UUID.randomUUID();
        EventFilters filters = new EventFilters(null, transactionId);

        // When
        boolean result = filters.hasTransactionId();

        // Then
        assertThat(result).isTrue();
    }

    @Test
    void shouldReturnFalseWhenTransactionIdIsNull() {
        // Given
        EventFilters filters = new EventFilters(null, null);

        // When
        boolean result = filters.hasTransactionId();

        // Then
        assertThat(result).isFalse();
    }

    @Test
    void shouldReturnTrueWhenBothIdsAreNull() {
        // Given
        EventFilters filters = new EventFilters(null, null);

        // When
        boolean result = filters.isEmpty();

        // Then
        assertThat(result).isTrue();
    }

    @Test
    void shouldReturnFalseWhenOrderIdIsNotNull() {
        // Given
        UUID orderId = UUID.randomUUID();
        EventFilters filters = new EventFilters(orderId, null);

        // When
        boolean result = filters.isEmpty();

        // Then
        assertThat(result).isFalse();
    }

    @Test
    void shouldReturnFalseWhenTransactionIdIsNotNull() {
        // Given
        UUID transactionId = UUID.randomUUID();
        EventFilters filters = new EventFilters(null, transactionId);

        // When
        boolean result = filters.isEmpty();

        // Then
        assertThat(result).isFalse();
    }

    @Test
    void shouldReturnFalseWhenBothIdsAreNotNull() {
        // Given
        UUID orderId = UUID.randomUUID();
        UUID transactionId = UUID.randomUUID();
        EventFilters filters = new EventFilters(orderId, transactionId);

        // When
        boolean result = filters.isEmpty();

        // Then
        assertThat(result).isFalse();
    }

    @Test
    void shouldCreateEventFiltersWithNoArgsConstructor() {
        // When
        EventFilters filters = new EventFilters();

        // Then
        assertThat(filters.getOrderId()).isNull();
        assertThat(filters.getTransactionId()).isNull();
    }

    @Test
    void shouldCreateEventFiltersWithAllArgsConstructor() {
        // Given
        UUID orderId = UUID.randomUUID();
        UUID transactionId = UUID.randomUUID();

        // When
        EventFilters filters = new EventFilters(orderId, transactionId);

        // Then
        assertThat(filters.getOrderId()).isEqualTo(orderId);
        assertThat(filters.getTransactionId()).isEqualTo(transactionId);
    }
}

