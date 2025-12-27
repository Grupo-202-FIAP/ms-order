package com.nextime.order.application.usecases.implementation.event;

import com.nextime.order.application.exception.InvalidEventException;
import com.nextime.order.application.gateways.EventRepositoryPort;
import com.nextime.order.infrastructure.persistence.document.Event;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.UUID;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class NotifyEventUseCaseImplTest {

    @Mock
    private EventRepositoryPort eventRepository;

    @InjectMocks
    private NotifyEventUseCaseImpl notifyEventUseCase;

    private Event event;

    @BeforeEach
    void setUp() {
        event = Event.builder()
                .id(UUID.randomUUID())
                .orderId(UUID.randomUUID())
                .build();
    }

    @Test
    void shouldNotifyEventSuccessfully() {
        // Given
        when(eventRepository.save(event)).thenReturn(event);

        // When
        notifyEventUseCase.execute(event);

        // Then
        verify(eventRepository).save(event);
    }

    @Test
    void shouldThrowInvalidEventExceptionWhenEventIsNull() {
        // When/Then
        assertThatThrownBy(() -> notifyEventUseCase.execute(null))
                .isInstanceOf(InvalidEventException.class)
                .hasMessageContaining("Evento não pode ser nulo");

        verify(eventRepository, never()).save(any());
    }

    @Test
    void shouldThrowInvalidEventExceptionWhenOrderIdIsNull() {
        // Given
        event.setOrderId(null);

        // When/Then
        assertThatThrownBy(() -> notifyEventUseCase.execute(event))
                .isInstanceOf(InvalidEventException.class)
                .hasMessageContaining("orderId é obrigatório");

        verify(eventRepository, never()).save(any());
    }
}

