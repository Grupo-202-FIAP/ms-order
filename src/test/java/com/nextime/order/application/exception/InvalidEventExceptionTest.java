package com.nextime.order.application.exception;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

class InvalidEventExceptionTest {

    @Test
    void shouldCreateInvalidEventExceptionWithMessage() {
        // Given
        String message = "Invalid event";

        // When
        InvalidEventException exception = new InvalidEventException(message);

        // Then
        assertThat(exception.getMessage()).isEqualTo(message);
    }
}

