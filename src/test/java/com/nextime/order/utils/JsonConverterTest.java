package com.nextime.order.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nextime.order.application.gateways.LoggerPort;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes para JsonConverter")
class JsonConverterTest {

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private LoggerPort logger;

    @InjectMocks
    private JsonConverter jsonConverter;

    private Event event;
    private String jsonString;

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

        jsonString = "{\"id\":\"" + event.getId() + "\",\"orderId\":\"" + orderId + "\"}";
    }

    @Test
    @DisplayName("Deve converter objeto para JSON com sucesso")
    void deveConverterObjetoParaJsonComSucesso() throws Exception {
        // Given
        when(objectMapper.writeValueAsString(any())).thenReturn(jsonString);

        // When
        String result = jsonConverter.toJson(event);

        // Then
        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(jsonString);
        verify(objectMapper, times(1)).writeValueAsString(event);
        verify(logger, never()).error(anyString(), any());
    }

    @Test
    @DisplayName("Deve lançar exceção quando houver erro ao converter para JSON")
    void deveLancarExcecaoQuandoErroAoConverterParaJson() throws Exception {
        // Given
        when(objectMapper.writeValueAsString(any())).thenThrow(new JsonProcessingException("Erro") {});

        // When & Then
        assertThatThrownBy(() -> jsonConverter.toJson(event))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Falha para converter objeto para JSON");

        verify(logger, times(1)).error(anyString(), any(Exception.class));
    }

    @Test
    @DisplayName("Deve converter JSON para Event com sucesso")
    void deveConverterJsonParaEventComSucesso() throws Exception {
        // Given
        when(objectMapper.readValue(anyString(), eq(Event.class))).thenReturn(event);

        // When
        Event result = jsonConverter.toEvent(jsonString);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(event.getId());
        assertThat(result.getOrderId()).isEqualTo(event.getOrderId());
        verify(objectMapper, times(1)).readValue(jsonString, Event.class);
        verify(logger, never()).error(anyString(), any());
    }

    @Test
    @DisplayName("Deve lançar exceção quando houver erro ao converter JSON para Event")
    void deveLancarExcecaoQuandoErroAoConverterJsonParaEvent() throws Exception {
        // Given
        String invalidJson = "invalid json";
        when(objectMapper.readValue(anyString(), eq(Event.class)))
                .thenThrow(new JsonProcessingException("Erro") {});

        // When & Then
        assertThatThrownBy(() -> jsonConverter.toEvent(invalidJson))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Falha para converter JSON para Event");

        verify(logger, times(1)).error(anyString(), any(Exception.class));
    }

    @Test
    @DisplayName("Deve converter diferentes tipos de objetos para JSON")
    void deveConverterDiferentesTiposObjetosParaJson() throws Exception {
        // Given
        Order order = Order.builder()
                .id(UUID.randomUUID())
                .identifier("ORD-1234")
                .build();

        String orderJson = "{\"id\":\"" + order.getId() + "\"}";
        when(objectMapper.writeValueAsString(any())).thenReturn(orderJson);

        // When
        String result = jsonConverter.toJson(order);

        // Then
        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(orderJson);
    }
}

