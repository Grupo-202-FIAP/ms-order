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
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SaveEventUseCaseImplTest {

    @Mock
    private EventRepositoryPort eventRepository;

    @InjectMocks
    private SaveEventUseCaseImpl saveEventUseCase;

    private Event event;

    @BeforeEach
    void setUp() {
        event = Event.builder()
                .id(UUID.randomUUID())
                .orderId(UUID.randomUUID())
                .build();
    }

    @Test
    void shouldSaveEventSuccessfully() {
        // Given
        when(eventRepository.save(event)).thenReturn(event);

        // When
        Event result = saveEventUseCase.execute(event);

        // Then
        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(event);
        verify(eventRepository).save(event);
    }

    @Test
    void shouldThrowInvalidEventExceptionWhenEventIsNull() {
        // When/Then
        assertThatThrownBy(() -> saveEventUseCase.execute(null))
                .isInstanceOf(InvalidEventException.class)
                .hasMessageContaining("Evento não pode ser nulo");

        verify(eventRepository, never()).save(any());
    }

    @Test
    void shouldThrowInvalidEventExceptionWhenOrderIdIsNull() {
        // Given
        event.setOrderId(null);

        // When/Then
        assertThatThrownBy(() -> saveEventUseCase.execute(event))
                .isInstanceOf(InvalidEventException.class)
                .hasMessageContaining("orderId é obrigatório");

        verify(eventRepository, never()).save(any());
    }
}

