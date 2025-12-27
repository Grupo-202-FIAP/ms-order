package com.nextime.order.infrastructure.adapters;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.assertj.core.api.Assertions.assertThatNoException;

@ExtendWith(MockitoExtension.class)
class LoggerRepositoryAdapterTest {

    @InjectMocks
    private LoggerRepositoryAdapter loggerRepositoryAdapter;

    @Test
    void shouldLogInfoSuccessfully() {
        // When/Then
        assertThatNoException().isThrownBy(() ->
                loggerRepositoryAdapter.info("Test message {}", "arg1")
        );
    }

    @Test
    void shouldLogDebugSuccessfully() {
        // When/Then
        assertThatNoException().isThrownBy(() ->
                loggerRepositoryAdapter.debug("Test message {}", "arg1")
        );
    }

    @Test
    void shouldLogWarnSuccessfully() {
        // When/Then
        assertThatNoException().isThrownBy(() ->
                loggerRepositoryAdapter.warn("Test message {}", "arg1")
        );
    }

    @Test
    void shouldLogErrorWithThrowableSuccessfully() {
        // Given
        Exception exception = new Exception("Test exception");

        // When/Then
        assertThatNoException().isThrownBy(() ->
                loggerRepositoryAdapter.error("Test message {}", exception, "arg1")
        );
    }

    @Test
    void shouldLogErrorWithoutThrowableSuccessfully() {
        // When/Then
        assertThatNoException().isThrownBy(() ->
                loggerRepositoryAdapter.error("Test message {}", "arg1")
        );
    }
}

