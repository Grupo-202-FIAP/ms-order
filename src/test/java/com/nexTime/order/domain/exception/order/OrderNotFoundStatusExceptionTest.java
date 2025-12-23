package com.nexTime.order.domain.exception.order;

import com.nexTime.order.domain.enums.OrderStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Testes para OrderNotFoundStatusException")
class OrderNotFoundStatusExceptionTest {

    @Test
    @DisplayName("Deve criar exceção com mensagem contendo status")
    void deveCriarExcecaoComMensagemContendoStatus() {
        // Given
        OrderStatus status = OrderStatus.CANCELLED;

        // When
        OrderNotFoundStatusException exception = new OrderNotFoundStatusException(status);

        // Then
        assertThat(exception).isNotNull();
        assertThat(exception.getMessage()).contains(status.toString());
        assertThat(exception.getMessage()).contains("Pedido não encontrado com o status");
    }

    @Test
    @DisplayName("Deve ser uma RuntimeException")
    void deveSerUmaRuntimeException() {
        // Given
        OrderStatus status = OrderStatus.PREPARING;

        // When
        OrderNotFoundStatusException exception = new OrderNotFoundStatusException(status);

        // Then
        assertThat(exception).isInstanceOf(RuntimeException.class);
    }
}

