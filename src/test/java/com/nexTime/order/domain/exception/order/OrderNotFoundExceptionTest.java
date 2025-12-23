package com.nexTime.order.domain.exception.order;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Testes para OrderNotFoundException")
class OrderNotFoundExceptionTest {

    @Test
    @DisplayName("Deve criar exceção com mensagem contendo orderId")
    void deveCriarExcecaoComMensagemContendoOrderId() {
        // Given
        UUID orderId = UUID.randomUUID();

        // When
        OrderNotFoundException exception = new OrderNotFoundException(orderId);

        // Then
        assertThat(exception).isNotNull();
        assertThat(exception.getMessage()).contains(orderId.toString());
        assertThat(exception.getMessage()).contains("Pedido não encontrado");
    }

    @Test
    @DisplayName("Deve ser uma RuntimeException")
    void deveSerUmaRuntimeException() {
        // Given
        UUID orderId = UUID.randomUUID();

        // When
        OrderNotFoundException exception = new OrderNotFoundException(orderId);

        // Then
        assertThat(exception).isInstanceOf(RuntimeException.class);
    }
}

