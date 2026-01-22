package com.nextime.order.infrastructure.persistence.document;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class HistoryTest {

    @Test
    void shouldCreateHistoryWithBuilder() {
        // Given
        String source = "test-source";
        String status = "test-status";
        String message = "test-message";
        LocalDateTime createdAt = LocalDateTime.now();

        // When
        History history = History.builder()
                .source(source)
                .status(status)
                .message(message)
                .createdAt(createdAt)
                .build();

        // Then
        assertThat(history.getSource()).isEqualTo(source);
        assertThat(history.getStatus()).isEqualTo(status);
        assertThat(history.getMessage()).isEqualTo(message);
        assertThat(history.getCreatedAt()).isEqualTo(createdAt);
    }

    @Test
    void shouldCreateHistoryWithNoArgsConstructor() {
        // When
        History history = new History();

        // Then
        assertThat(history).isNotNull();
    }

    @Test
    void shouldCreateHistoryWithAllArgsConstructor() {
        // Given
        String source = "test-source";
        String status = "test-status";
        String message = "test-message";
        LocalDateTime createdAt = LocalDateTime.now();

        // When
        History history = new History(source, status, message, createdAt);

        // Then
        assertThat(history.getSource()).isEqualTo(source);
        assertThat(history.getStatus()).isEqualTo(status);
        assertThat(history.getMessage()).isEqualTo(message);
        assertThat(history.getCreatedAt()).isEqualTo(createdAt);
    }

    @Test
    void shouldSetAndGetProperties() {
        // Given
        History history = new History();
        String source = "test-source";
        String status = "test-status";
        String message = "test-message";
        LocalDateTime createdAt = LocalDateTime.now();

        // When
        history.setSource(source);
        history.setStatus(status);
        history.setMessage(message);
        history.setCreatedAt(createdAt);

        // Then
        assertThat(history.getSource()).isEqualTo(source);
        assertThat(history.getStatus()).isEqualTo(status);
        assertThat(history.getMessage()).isEqualTo(message);
        assertThat(history.getCreatedAt()).isEqualTo(createdAt);
    }
}

