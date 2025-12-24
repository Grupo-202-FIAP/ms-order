package com.nexTime.msorder.domain;

import com.nextime.order.domain.EventFilters;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Testes para EventFilters")
class EventFiltersTest {

    @Test
    @DisplayName("Deve criar EventFilters com orderId e transactionId")
    void deveCriarEventFiltersComOrderIdETransactionId() {
        // Given
        UUID orderId = UUID.randomUUID();
        UUID transactionId = UUID.randomUUID();

        // When
        EventFilters filters = new EventFilters(orderId, transactionId);

        // Then
        assertThat(filters.getOrderId()).isEqualTo(orderId);
        assertThat(filters.getTransactionId()).isEqualTo(transactionId);
    }

    @Test
    @DisplayName("Deve criar EventFilters vazio")
    void deveCriarEventFiltersVazio() {
        // When
        EventFilters filters = new EventFilters();

        // Then
        assertThat(filters.getOrderId()).isNull();
        assertThat(filters.getTransactionId()).isNull();
    }

    @Test
    @DisplayName("Deve permitir definir orderId e transactionId")
    void devePermitirDefinirOrderIdETransactionId() {
        // Given
        EventFilters filters = new EventFilters();
        UUID orderId = UUID.randomUUID();
        UUID transactionId = UUID.randomUUID();

        // When
        filters.setOrderId(orderId);
        filters.setTransactionId(transactionId);

        // Then
        assertThat(filters.getOrderId()).isEqualTo(orderId);
        assertThat(filters.getTransactionId()).isEqualTo(transactionId);
    }

    @Test
    @DisplayName("Deve permitir definir apenas orderId")
    void devePermitirDefinirApenasOrderId() {
        // Given
        EventFilters filters = new EventFilters();
        UUID orderId = UUID.randomUUID();

        // When
        filters.setOrderId(orderId);

        // Then
        assertThat(filters.getOrderId()).isEqualTo(orderId);
        assertThat(filters.getTransactionId()).isNull();
    }

    @Test
    @DisplayName("Deve permitir definir apenas transactionId")
    void devePermitirDefinirApenasTransactionId() {
        // Given
        EventFilters filters = new EventFilters();
        UUID transactionId = UUID.randomUUID();

        // When
        filters.setTransactionId(transactionId);

        // Then
        assertThat(filters.getOrderId()).isNull();
        assertThat(filters.getTransactionId()).isEqualTo(transactionId);
    }
}

