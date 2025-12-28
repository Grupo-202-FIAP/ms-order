package com.nextime.order.domain.exception.event;

import org.junit.jupiter.api.Test;
import java.util.UUID;
import static org.assertj.core.api.Assertions.assertThat;

class EventNotFoundOrderIdExceptionTest {

    @Test
    void shouldCreateEventNotFoundOrderIdExceptionWithOrderId() {
        // Given
        UUID orderId = UUID.randomUUID();

        // When
        EventNotFoundOrderIdException exception = new EventNotFoundOrderIdException(orderId);

        // Then
        assertThat(exception.getMessage()).contains("Evento não encontrado para o orderId:");
        assertThat(exception.getMessage()).contains(orderId.toString());
    }
}


