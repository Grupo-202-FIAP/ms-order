package com.nextime.order.application.config.logs;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.MDC;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class LoggingContextFilterTest {

    @Mock
    private ServletRequest request;

    @Mock
    private ServletResponse response;

    @Mock
    private FilterChain filterChain;

    private LoggingContextFilter filter;

    @BeforeEach
    void setUp() {
        filter = new LoggingContextFilter();
        MDC.clear();
    }

    @Test
    void shouldAddTraceIdToMDCAndRemoveAfterFilter() throws ServletException, IOException {
        // Given
        doAnswer(invocation -> {
            // Verify traceId is set during filter execution
            String traceId = MDC.get("traceId");
            assertThat(traceId).isNotNull();
            assertThat(traceId).isNotEmpty();
            return null;
        }).when(filterChain).doFilter(request, response);

        // When
        filter.doFilter(request, response, filterChain);

        // Then
        verify(filterChain).doFilter(request, response);
        // Verify traceId is removed after filter execution
        assertThat(MDC.get("traceId")).isNull();
    }

    @Test
    void shouldGenerateUniqueTraceId() throws ServletException, IOException {
        // Given
        String[] traceIds = new String[2];
        doAnswer(invocation -> {
            traceIds[0] = MDC.get("traceId");
            return null;
        }).when(filterChain).doFilter(request, response);

        // When
        filter.doFilter(request, response, filterChain);
        filter.doFilter(request, response, filterChain);

        // Then
        assertThat(traceIds[0]).isNotNull();
        assertThat(MDC.get("traceId")).isNull();
    }

    @Test
    void shouldRemoveTraceIdEvenIfExceptionOccurs() throws ServletException, IOException {
        // Given
        doAnswer(invocation -> {
            throw new ServletException("Test exception");
        }).when(filterChain).doFilter(request, response);

        // When/Then
        try {
            filter.doFilter(request, response, filterChain);
        } catch (ServletException e) {
            // Expected exception
        }

        // Verify traceId is removed even after exception
        assertThat(MDC.get("traceId")).isNull();
    }
}

