package com.nextime.order.application.exception;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

class MessagePublishExceptionTest {

    @Test
    void shouldCreateMessagePublishExceptionWithMessage() {
        // Given
        String message = "Publish error";

        // When
        MessagePublishException exception = new MessagePublishException(message);

        // Then
        assertThat(exception.getMessage()).isEqualTo(message);
    }

    @Test
    void shouldCreateMessagePublishExceptionWithMessageAndCause() {
        // Given
        String message = "Publish error";
        Throwable cause = new RuntimeException("Root cause");

        // When
        MessagePublishException exception = new MessagePublishException(message, cause);

        // Then
        assertThat(exception.getMessage()).isEqualTo(message);
        assertThat(exception.getCause()).isEqualTo(cause);
    }
}


