package com.nexTime.msorder.domain.exception.event;

import com.nextime.order.domain.exception.event.EventNotFoundTransactionIdException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Testes para EventNotFoundTransactionIdException")
class EventNotFoundTransactionIdExceptionTest {

    @Test
    @DisplayName("Deve criar exceção com mensagem contendo transactionId")
    void deveCriarExcecaoComMensagemContendoTransactionId() {
        // Given
        UUID transactionId = UUID.randomUUID();

        // When
        EventNotFoundTransactionIdException exception = new EventNotFoundTransactionIdException(transactionId);

        // Then
        assertThat(exception).isNotNull();
        assertThat(exception.getMessage()).contains(transactionId.toString());
        assertThat(exception.getMessage()).contains("Não foi encontrado um evento com o transaction ID");
    }

    @Test
    @DisplayName("Deve ser uma RuntimeException")
    void deveSerUmaRuntimeException() {
        // Given
        UUID transactionId = UUID.randomUUID();

        // When
        EventNotFoundTransactionIdException exception = new EventNotFoundTransactionIdException(transactionId);

        // Then
        assertThat(exception).isInstanceOf(RuntimeException.class);
    }
}

