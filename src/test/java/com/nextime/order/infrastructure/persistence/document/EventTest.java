package com.nextime.order.infrastructure.persistence.document;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class EventTest {

    @Test
    void shouldCreateEventWithBuilder() {
        // Given
        UUID id = UUID.randomUUID();
        UUID transactionId = UUID.randomUUID();
        UUID orderId = UUID.randomUUID();
        Order order = Order.builder().build();
        List<History> history = new ArrayList<>();

        // When
        Event event = Event.builder()
                .id(id)
                .transactionId(transactionId)
                .orderId(orderId)
                .payload(order)
                .source("test-source")
                .status("test-status")
                .history(history)
                .createdAt(LocalDateTime.now())
                .build();

        // Then
        assertThat(event.getId()).isEqualTo(id);
        assertThat(event.getTransactionId()).isEqualTo(transactionId);
        assertThat(event.getOrderId()).isEqualTo(orderId);
        assertThat(event.getPayload()).isEqualTo(order);
        assertThat(event.getSource()).isEqualTo("test-source");
        assertThat(event.getStatus()).isEqualTo("test-status");
        assertThat(event.getHistory()).isEqualTo(history);
        assertThat(event.getCreatedAt()).isNotNull();
    }

    @Test
    void shouldCreateEventWithNoArgsConstructor() {
        // When
        Event event = new Event();

        // Then
        assertThat(event).isNotNull();
    }

    @Test
    void shouldCreateEventWithAllArgsConstructor() {
        // Given
        UUID id = UUID.randomUUID();
        UUID transactionId = UUID.randomUUID();
        UUID orderId = UUID.randomUUID();
        Order order = Order.builder().build();
        List<History> history = new ArrayList<>();
        LocalDateTime createdAt = LocalDateTime.now();

        // When
        Event event = new Event(id, transactionId, orderId, order, "source", "status", history, createdAt);

        // Then
        assertThat(event.getId()).isEqualTo(id);
        assertThat(event.getTransactionId()).isEqualTo(transactionId);
        assertThat(event.getOrderId()).isEqualTo(orderId);
        assertThat(event.getPayload()).isEqualTo(order);
        assertThat(event.getSource()).isEqualTo("source");
        assertThat(event.getStatus()).isEqualTo("status");
        assertThat(event.getHistory()).isEqualTo(history);
        assertThat(event.getCreatedAt()).isEqualTo(createdAt);
    }

    @Test
    void shouldSetAndGetProperties() {
        // Given
        Event event = new Event();
        UUID id = UUID.randomUUID();
        UUID transactionId = UUID.randomUUID();
        UUID orderId = UUID.randomUUID();
        Order order = Order.builder().build();

        // When
        event.setId(id);
        event.setTransactionId(transactionId);
        event.setOrderId(orderId);
        event.setPayload(order);
        event.setSource("source");
        event.setStatus("status");
        event.setHistory(new ArrayList<>());
        event.setCreatedAt(LocalDateTime.now());

        // Then
        assertThat(event.getId()).isEqualTo(id);
        assertThat(event.getTransactionId()).isEqualTo(transactionId);
        assertThat(event.getOrderId()).isEqualTo(orderId);
        assertThat(event.getPayload()).isEqualTo(order);
        assertThat(event.getSource()).isEqualTo("source");
        assertThat(event.getStatus()).isEqualTo("status");
        assertThat(event.getHistory()).isNotNull();
        assertThat(event.getCreatedAt()).isNotNull();
    }
}

