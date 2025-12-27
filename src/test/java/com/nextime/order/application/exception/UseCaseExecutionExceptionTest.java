package com.nextime.order.application.exception;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

class UseCaseExecutionExceptionTest {

    @Test
    void shouldCreateUseCaseExecutionExceptionWithMessage() {
        // Given
        String message = "Execution error";

        // When
        UseCaseExecutionException exception = new UseCaseExecutionException(message);

        // Then
        assertThat(exception.getMessage()).isEqualTo(message);
    }
}

