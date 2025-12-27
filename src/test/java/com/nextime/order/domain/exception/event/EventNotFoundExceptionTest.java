package com.nextime.order.domain.exception.event;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

class EventNotFoundExceptionTest {

    @Test
    void shouldCreateEventNotFoundException() {
        // When
        EventNotFoundException exception = new EventNotFoundException();

        // Then
        assertThat(exception.getMessage()).isEqualTo("Evento não encontrado");
    }
}

