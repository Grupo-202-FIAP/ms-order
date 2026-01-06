package com.nextime.order.domain.exception.event;

import org.junit.jupiter.api.Test;
import java.util.UUID;
import static org.assertj.core.api.Assertions.assertThat;

class EventNotFoundTransactionIdExceptionTest {

    @Test
    void shouldCreateEventNotFoundTransactionIdExceptionWithTransactionId() {
        // Given
        UUID transactionId = UUID.randomUUID();

        // When
        EventNotFoundTransactionIdException exception = new EventNotFoundTransactionIdException(transactionId);

        // Then
        assertThat(exception.getMessage()).contains("Evento não encontrado para o transactionId:");
        assertThat(exception.getMessage()).contains(transactionId.toString());
    }
}



