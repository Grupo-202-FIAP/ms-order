package com.nexTime.msorder.domain.exception.event;

import com.nextime.order.domain.exception.event.EventNotFoundOrderIdException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Testes para EventNotFoundOrderIdException")
class EventNotFoundOrderIdExceptionTest {

    @Test
    @DisplayName("Deve criar exceção com mensagem contendo orderId")
    void deveCriarExcecaoComMensagemContendoOrderId() {
        // Given
        UUID orderId = UUID.randomUUID();

        // When
        EventNotFoundOrderIdException exception = new EventNotFoundOrderIdException(orderId);

        // Then
        assertThat(exception).isNotNull();
        assertThat(exception.getMessage()).contains(orderId.toString());
        assertThat(exception.getMessage()).contains("Não foi encontrado um evento com o order id");
    }

    @Test
    @DisplayName("Deve ser uma RuntimeException")
    void deveSerUmaRuntimeException() {
        // Given
        UUID orderId = UUID.randomUUID();

        // When
        EventNotFoundOrderIdException exception = new EventNotFoundOrderIdException(orderId);

        // Then
        assertThat(exception).isInstanceOf(RuntimeException.class);
    }
}

