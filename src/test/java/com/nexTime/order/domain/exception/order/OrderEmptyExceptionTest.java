package com.nexTime.order.domain.exception.order;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Testes para OrderEmptyException")
class OrderEmptyExceptionTest {

    @Test
    @DisplayName("Deve criar exceção com mensagem correta")
    void deveCriarExcecaoComMensagemCorreta() {
        // When
        OrderEmptyException exception = new OrderEmptyException();

        // Then
        assertThat(exception).isNotNull();
        assertThat(exception.getMessage()).isEqualTo("[Exception] [Order] Nenhum pedido encontrado");
    }

    @Test
    @DisplayName("Deve ser uma RuntimeException")
    void deveSerUmaRuntimeException() {
        // When
        OrderEmptyException exception = new OrderEmptyException();

        // Then
        assertThat(exception).isInstanceOf(RuntimeException.class);
    }
}

