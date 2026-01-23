package com.nextime.order.domain.exception;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ExceptionDetailTest {

    @Test
    void shouldCreateExceptionDetail() {
        // Given
        Integer statusCode = 404;
        String message = "Not Found";

        // When
        ExceptionDetail exceptionDetail = new ExceptionDetail(statusCode, message);

        // Then
        assertThat(exceptionDetail.statusCode()).isEqualTo(statusCode);
        assertThat(exceptionDetail.message()).isEqualTo(message);
    }

    @Test
    void shouldCreateExceptionDetailWithDifferentValues() {
        // Given
        Integer statusCode = 500;
        String message = "Internal Server Error";

        // When
        ExceptionDetail exceptionDetail = new ExceptionDetail(statusCode, message);

        // Then
        assertThat(exceptionDetail.statusCode()).isEqualTo(statusCode);
        assertThat(exceptionDetail.message()).isEqualTo(message);
    }
}

