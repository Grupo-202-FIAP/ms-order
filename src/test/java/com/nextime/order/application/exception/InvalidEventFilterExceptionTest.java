package com.nextime.order.application.exception;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

class InvalidEventFilterExceptionTest {

    @Test
    void shouldCreateInvalidEventFilterExceptionWithMessage() {
        // Given
        String message = "Invalid filter";

        // When
        InvalidEventFilterException exception = new InvalidEventFilterException(message);

        // Then
        assertThat(exception.getMessage()).isEqualTo(message);
    }
}



