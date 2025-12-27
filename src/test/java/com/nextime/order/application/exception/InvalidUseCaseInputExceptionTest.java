package com.nextime.order.application.exception;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

class InvalidUseCaseInputExceptionTest {

    @Test
    void shouldCreateInvalidUseCaseInputExceptionWithMessage() {
        // Given
        String message = "Invalid input";

        // When
        InvalidUseCaseInputException exception = new InvalidUseCaseInputException(message);

        // Then
        assertThat(exception.getMessage()).isEqualTo(message);
    }
}

