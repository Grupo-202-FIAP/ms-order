package com.nextime.order.application.exception;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

class JsonConversionExceptionTest {

    @Test
    void shouldCreateJsonConversionExceptionWithMessage() {
        // Given
        String message = "Conversion error";

        // When
        JsonConversionException exception = new JsonConversionException(message);

        // Then
        assertThat(exception.getMessage()).isEqualTo(message);
    }

    @Test
    void shouldCreateJsonConversionExceptionWithMessageAndCause() {
        // Given
        String message = "Conversion error";
        Throwable cause = new RuntimeException("Root cause");

        // When
        JsonConversionException exception = new JsonConversionException(message, cause);

        // Then
        assertThat(exception.getMessage()).isEqualTo(message);
        assertThat(exception.getCause()).isEqualTo(cause);
    }
}

