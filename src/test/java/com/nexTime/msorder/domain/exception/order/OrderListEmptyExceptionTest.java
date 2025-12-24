package com.nexTime.msorder.domain.exception.order;

import com.nextime.order.domain.exception.order.OrderListEmptyException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Testes para OrderListEmptyException")
class OrderListEmptyExceptionTest {

    @Test
    @DisplayName("Deve criar exceção com mensagem correta")
    void deveCriarExcecaoComMensagemCorreta() {
        // When
        OrderListEmptyException exception = new OrderListEmptyException();

        // Then
        assertThat(exception).isNotNull();
        assertThat(exception.getMessage()).isEqualTo("[Exception] [Order] A lista de items não pode ser vazia");
    }

    @Test
    @DisplayName("Deve ser uma RuntimeException")
    void deveSerUmaRuntimeException() {
        // When
        OrderListEmptyException exception = new OrderListEmptyException();

        // Then
        assertThat(exception).isInstanceOf(RuntimeException.class);
    }
}

