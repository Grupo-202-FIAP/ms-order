package com.nextime.order.application.exception;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

class ValidationExceptionTest {

    @Test
    void shouldCreateValidationExceptionWithMessage() {
        // Given
        String message = "Validation error";

        // When
        ValidationException exception = new ValidationException(message);

        // Then
        assertThat(exception.getMessage()).isEqualTo(message);
    }
}



