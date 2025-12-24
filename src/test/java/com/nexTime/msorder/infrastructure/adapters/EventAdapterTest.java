package com.nexTime.msorder.infrastructure.adapters;

import com.nextime.order.application.gateways.LoggerPort;
import com.nextime.order.infrastructure.adapters.EventAdapter;
import com.nextime.order.infrastructure.persistence.document.Event;
import com.nextime.order.infrastructure.persistence.document.Order;
import com.nextime.order.infrastructure.persistence.repository.IEventRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes para EventAdapter")
class EventAdapterTest {

    @Mock
    private IEventRepository eventRepository;

    @Mock
    private LoggerPort logger;

    @InjectMocks
    private EventAdapter eventAdapter;

    private Event event;
    private UUID orderId;
    private UUID transactionId;

    @BeforeEach
    void setUp() {
        orderId = UUID.randomUUID();
        transactionId = UUID.randomUUID();
        event = Event.builder()
                .id(UUID.randomUUID())
                .orderId(orderId)
                .transactionId(transactionId)
                .payload(Order.builder().id(orderId).build())
                .createdAt(LocalDateTime.now())
                .build();
    }

    @Test
    @DisplayName("Deve salvar evento com sucesso")
    void deveSalvarEventoComSucesso() {
        // Given
        when(eventRepository.save(any(Event.class))).thenReturn(event);

        // When
        Event result = eventAdapter.save(event);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(event.getId());
        assertThat(result.getOrderId()).isEqualTo(orderId);
        verify(eventRepository, times(1)).save(any(Event.class));
        verify(logger, times(1)).info(anyString(), any());
    }

    @Test
    @DisplayName("Deve lançar exceção quando houver erro ao salvar evento")
    void deveLancarExcecaoQuandoErroAoSalvar() {
        // Given
        when(eventRepository.save(any(Event.class))).thenThrow(new RuntimeException("Erro no banco"));

        // When & Then
        assertThatThrownBy(() -> eventAdapter.save(event))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Erro ao salvar evento");

        verify(logger, times(1)).error(anyString(), any(Exception.class));
    }


    @Test
    @DisplayName("Deve definir createdAt ao notificar evento")
    void deveDefinirCreatedAtAoNotificarEvento() {
        // Given
        Event eventWithoutDate = Event.builder()
                .id(UUID.randomUUID())
                .orderId(orderId)
                .transactionId(transactionId)
                .build();

        when(eventRepository.save(any(Event.class))).thenAnswer(invocation -> {
            Event savedEvent = invocation.getArgument(0);
            assertThat(savedEvent.getCreatedAt()).isNotNull();
            return savedEvent;
        });

        // When
        eventAdapter.notifyEvent(eventWithoutDate);

        // Then
        verify(eventRepository, times(1)).save(any(Event.class));
    }
}

