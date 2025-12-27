package com.nextime.order.infrastructure.messaging.consumer;

import com.nextime.order.application.exception.JsonConversionException;
import com.nextime.order.application.exception.UseCaseExecutionException;
import com.nextime.order.application.gateways.LoggerRepositoryPort;
import com.nextime.order.application.usecases.interfaces.event.NotifyEventUseCase;
import com.nextime.order.infrastructure.exception.RepositoryException;
import com.nextime.order.infrastructure.persistence.document.Event;
import com.nextime.order.utils.JsonConverter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.UUID;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EventConsumerTest {

    @Mock
    private LoggerRepositoryPort logger;

    @Mock
    private JsonConverter jsonConverter;

    @Mock
    private NotifyEventUseCase notifyEventUseCase;

    @InjectMocks
    private EventConsumer eventConsumer;

    private String payload;
    private Event event;

    @BeforeEach
    void setUp() {
        payload = "{\"id\":\"123\"}";
        event = Event.builder()
                .id(UUID.randomUUID())
                .orderId(UUID.randomUUID())
                .build();
    }

    @Test
    void shouldConsumeMessageSuccessfully() {
        // Given
        when(jsonConverter.toEvent(payload)).thenReturn(event);

        // When
        eventConsumer.consumeMessage(payload);

        // Then
        verify(logger).info(anyString(), anyString());
        verify(jsonConverter).toEvent(payload);
        verify(notifyEventUseCase).execute(event);
        verify(logger).info(anyString(), any(Event.class));
    }

    @Test
    void shouldHandleJsonConversionException() {
        // Given
        JsonConversionException exception = new JsonConversionException("Invalid JSON");
        when(jsonConverter.toEvent(payload)).thenThrow(exception);

        // When
        eventConsumer.consumeMessage(payload);

        // Then
        verify(logger).error(anyString(), any(JsonConversionException.class));
        verify(notifyEventUseCase, never()).execute(any());
    }

    @Test
    void shouldRethrowRepositoryException() {
        // Given
        RepositoryException exception = new RepositoryException("Repository error");
        when(jsonConverter.toEvent(payload)).thenReturn(event);
        org.mockito.Mockito.doThrow(exception).when(notifyEventUseCase).execute(event);

        // When/Then
        assertThatThrownBy(() -> eventConsumer.consumeMessage(payload))
                .isInstanceOf(RepositoryException.class);

        verify(logger).error(anyString(), any(RepositoryException.class));
    }

    @Test
    void shouldRethrowUseCaseExecutionException() {
        // Given
        UseCaseExecutionException exception = new UseCaseExecutionException("Use case error");
        when(jsonConverter.toEvent(payload)).thenReturn(event);
        org.mockito.Mockito.doThrow(exception).when(notifyEventUseCase).execute(event);

        // When/Then
        assertThatThrownBy(() -> eventConsumer.consumeMessage(payload))
                .isInstanceOf(UseCaseExecutionException.class);

        verify(logger).error(anyString(), any(UseCaseExecutionException.class));
    }

    @Test
    void shouldRethrowGenericException() {
        // Given
        RuntimeException exception = new RuntimeException("Generic error");
        when(jsonConverter.toEvent(payload)).thenReturn(event);
        org.mockito.Mockito.doThrow(exception).when(notifyEventUseCase).execute(event);

        // When/Then
        assertThatThrownBy(() -> eventConsumer.consumeMessage(payload))
                .isInstanceOf(RuntimeException.class);

        verify(logger).error(anyString(), any(Exception.class));
    }
}

