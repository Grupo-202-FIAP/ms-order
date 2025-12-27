package com.nextime.order.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nextime.order.application.exception.JsonConversionException;
import com.nextime.order.application.gateways.LoggerRepositoryPort;
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
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JsonConverterTest {

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private LoggerRepositoryPort logger;

    @InjectMocks
    private JsonConverter jsonConverter;

    private Event testEvent;

    @BeforeEach
    void setUp() {
        testEvent = Event.builder()
                .id(UUID.randomUUID())
                .orderId(UUID.randomUUID())
                .build();
    }

    @Test
    void shouldConvertObjectToJsonSuccessfully() throws JsonProcessingException {
        // Given
        String expectedJson = "{\"id\":\"123\"}";
        when(objectMapper.writeValueAsString(any())).thenReturn(expectedJson);

        // When
        String result = jsonConverter.toJson(testEvent);

        // Then
        assertThat(result).isEqualTo(expectedJson);
        verify(objectMapper).writeValueAsString(testEvent);
    }

    @Test
    void shouldThrowJsonConversionExceptionWhenToJsonFails() throws JsonProcessingException {
        // Given
        JsonProcessingException exception = new JsonProcessingException("Error") {};
        when(objectMapper.writeValueAsString(any())).thenThrow(exception);

        // When/Then
        assertThatThrownBy(() -> jsonConverter.toJson(testEvent))
                .isInstanceOf(JsonConversionException.class)
                .hasMessageContaining("Erro ao converter objeto para JSON")
                .hasCause(exception);

        verify(logger).error(anyString(), any(Exception.class));
    }

    @Test
    void shouldConvertJsonToEventSuccessfully() throws JsonProcessingException {
        // Given
        String json = "{\"id\":\"123\"}";
        when(objectMapper.readValue(json, Event.class)).thenReturn(testEvent);

        // When
        Event result = jsonConverter.toEvent(json);

        // Then
        assertThat(result).isEqualTo(testEvent);
        verify(objectMapper).readValue(json, Event.class);
    }

    @Test
    void shouldThrowJsonConversionExceptionWhenToEventFails() throws JsonProcessingException {
        // Given
        String json = "invalid json";
        JsonProcessingException exception = new JsonProcessingException("Error") {};
        when(objectMapper.readValue(json, Event.class)).thenThrow(exception);

        // When/Then
        assertThatThrownBy(() -> jsonConverter.toEvent(json))
                .isInstanceOf(JsonConversionException.class)
                .hasMessageContaining("Erro ao converter JSON para Event")
                .hasCause(exception);

        verify(logger).error(anyString(), any(Exception.class));
    }
}

