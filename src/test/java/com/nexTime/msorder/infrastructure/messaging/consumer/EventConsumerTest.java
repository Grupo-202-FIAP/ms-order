package com.nexTime.msorder.infrastructure.messaging.consumer;

import com.nextime.order.application.gateways.LoggerPort;
import com.nextime.order.infrastructure.adapters.EventAdapter;
import com.nextime.order.infrastructure.messaging.consumer.EventConsumer;
import com.nextime.order.infrastructure.persistence.document.Event;
import com.nextime.order.infrastructure.persistence.document.Order;
import com.nextime.order.utils.JsonConverter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes para EventConsumer")
class EventConsumerTest {

    @Mock
    private LoggerPort logger;

    @Mock
    private JsonConverter jsonConverter;

    @Mock
    private EventAdapter eventAdapter;

    @InjectMocks
    private EventConsumer eventConsumer;

    private String payload;
    private Event event;

    @BeforeEach
    void setUp() {
        UUID orderId = UUID.randomUUID();
        event = Event.builder()
                .id(UUID.randomUUID())
                .orderId(orderId)
                .transactionId(UUID.randomUUID())
                .payload(Order.builder().id(orderId).build())
                .createdAt(LocalDateTime.now())
                .build();

        payload = "{\"id\":\"" + event.getId() + "\",\"orderId\":\"" + orderId + "\"}";
    }

    @Test
    @DisplayName("Deve consumir mensagem com sucesso")
    void deveConsumirMensagemComSucesso() {
        // Given
        when(jsonConverter.toEvent(anyString())).thenReturn(event);
        doNothing().when(eventAdapter).notifyEvent(any(Event.class));

        // When
        eventConsumer.consumeMessage(payload);

        // Then
        verify(jsonConverter, times(1)).toEvent(payload);
        verify(eventAdapter, times(1)).notifyEvent(event);
        verify(logger, times(2)).info(anyString(), any());
        verify(logger, never()).error(anyString(), any());
    }

    @Test
    @DisplayName("Deve tratar exceção ao consumir mensagem")
    void deveTratarExcecaoAoConsumirMensagem() {
        // Given
        when(jsonConverter.toEvent(anyString())).thenThrow(new RuntimeException("Erro ao converter"));

        // When
        eventConsumer.consumeMessage(payload);

        // Then
        verify(jsonConverter, times(1)).toEvent(payload);
        verify(eventAdapter, never()).notifyEvent(any(Event.class));
        verify(logger, times(1)).info(anyString(), any());
        verify(logger, times(1)).error(anyString(), anyString());
    }

    @Test
    @DisplayName("Deve logar informações ao consumir mensagem")
    void deveLogarInformacoesAoConsumirMensagem() {
        // Given
        when(jsonConverter.toEvent(anyString())).thenReturn(event);
        doNothing().when(eventAdapter).notifyEvent(any(Event.class));

        // When
        eventConsumer.consumeMessage(payload);

        // Then
        verify(logger, times(1)).info(contains("[consumeMessage] Consumindo mensagem"), eq(payload));
        verify(logger, times(1)).info(contains("[consumeMessage] Mensagem consumida"), any(Event.class));
    }
}

