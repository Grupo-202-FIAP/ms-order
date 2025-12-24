package com.nextime.order.domain.exception.event;

import com.nextime.order.domain.exception.event.EventNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Testes para EventNotFoundException")
class EventNotFoundExceptionTest {

    @Test
    @DisplayName("Deve criar exceção com mensagem correta")
    void deveCriarExcecaoComMensagemCorreta() {
        // When
        EventNotFoundException exception = new EventNotFoundException();

        // Then
        assertThat(exception).isNotNull();
        assertThat(exception.getMessage()).isEqualTo("[Exception] [Event] Evento não encontrado ");
    }

    @Test
    @DisplayName("Deve ser uma RuntimeException")
    void deveSerUmaRuntimeException() {
        // When
        EventNotFoundException exception = new EventNotFoundException();

        // Then
        assertThat(exception).isInstanceOf(RuntimeException.class);
    }
}

