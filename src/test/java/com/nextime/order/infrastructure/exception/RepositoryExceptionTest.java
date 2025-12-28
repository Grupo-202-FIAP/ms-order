package com.nextime.order.infrastructure.exception;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

class RepositoryExceptionTest {

    @Test
    void shouldCreateRepositoryExceptionWithMessage() {
        // Given
        String message = "Repository error";

        // When
        RepositoryException exception = new RepositoryException(message);

        // Then
        assertThat(exception.getMessage()).isEqualTo(message);
    }

    @Test
    void shouldCreateRepositoryExceptionWithMessageAndCause() {
        // Given
        String message = "Repository error";
        Throwable cause = new RuntimeException("Root cause");

        // When
        RepositoryException exception = new RepositoryException(message, cause);

        // Then
        assertThat(exception.getMessage()).isEqualTo(message);
        assertThat(exception.getCause()).isEqualTo(cause);
    }
}


