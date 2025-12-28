package com.nextime.order.infrastructure.controller.exception;

import com.nextime.order.application.exception.InvalidUseCaseInputException;
import com.nextime.order.application.exception.UseCaseExecutionException;
import com.nextime.order.application.exception.ValidationException;
import com.nextime.order.domain.enums.OrderStatus;
import com.nextime.order.domain.exception.event.EventNotFoundException;
import com.nextime.order.domain.exception.event.EventNotFoundOrderIdException;
import com.nextime.order.domain.exception.event.EventNotFoundTransactionIdException;
import com.nextime.order.domain.exception.order.OrderEmptyException;
import com.nextime.order.domain.exception.order.OrderListEmptyException;
import com.nextime.order.domain.exception.order.OrderNotFoundException;
import com.nextime.order.domain.exception.order.OrderNotFoundStatusException;
import com.nextime.order.infrastructure.exception.RepositoryException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import java.util.UUID;
import static org.assertj.core.api.Assertions.assertThat;

class ExceptionGlobalHandlerTest {

    private ExceptionGlobalHandler exceptionGlobalHandler;

    @BeforeEach
    void setUp() {
        exceptionGlobalHandler = new ExceptionGlobalHandler();
    }

    @Test
    void shouldHandleEventNotFoundException() {
        // Given
        EventNotFoundException exception = new EventNotFoundException();

        // When
        ProblemDetail result = exceptionGlobalHandler.handleEventNotFound(exception);

        // Then
        assertThat(result.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
        assertThat(result.getTitle()).isEqualTo("Evento não encontrado");
        assertThat(result.getDetail()).isEqualTo("Evento não encontrado");
    }

    @Test
    void shouldHandleEventNotFoundOrderIdException() {
        // Given
        UUID orderId = UUID.randomUUID();
        EventNotFoundOrderIdException exception = new EventNotFoundOrderIdException(orderId);

        // When
        ProblemDetail result = exceptionGlobalHandler.handleEventNotFoundByOrderId(exception);

        // Then
        assertThat(result.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
        assertThat(result.getTitle()).isEqualTo("Evento não encontrado");
        assertThat(result.getDetail()).contains("Evento não encontrado para o orderId:");
    }

    @Test
    void shouldHandleEventNotFoundTransactionIdException() {
        // Given
        UUID transactionId = UUID.randomUUID();
        EventNotFoundTransactionIdException exception = new EventNotFoundTransactionIdException(transactionId);

        // When
        ProblemDetail result = exceptionGlobalHandler.handleEventNotFoundByTransactionId(exception);

        // Then
        assertThat(result.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
        assertThat(result.getTitle()).isEqualTo("Evento não encontrado");
        assertThat(result.getDetail()).contains("Evento não encontrado para o transactionId:");
    }

    @Test
    void shouldHandleOrderEmptyException() {
        // Given
        OrderEmptyException exception = new OrderEmptyException();

        // When
        ProblemDetail result = exceptionGlobalHandler.handleOrderEmpty(exception);

        // Then
        assertThat(result.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
        assertThat(result.getTitle()).isEqualTo("Pedido não encontrado");
        assertThat(result.getDetail()).isEqualTo("Nenhum pedido encontrado");
    }

    @Test
    void shouldHandleOrderListEmptyException() {
        // Given
        OrderListEmptyException exception = new OrderListEmptyException();

        // When
        ProblemDetail result = exceptionGlobalHandler.handleOrderListEmpty(exception);

        // Then
        assertThat(result.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(result.getTitle()).isEqualTo("Lista de itens inválida");
        assertThat(result.getDetail()).isEqualTo("A lista de itens do pedido não pode ser vazia");
    }

    @Test
    void shouldHandleOrderNotFoundException() {
        // Given
        UUID orderId = UUID.randomUUID();
        OrderNotFoundException exception = new OrderNotFoundException(orderId);

        // When
        ProblemDetail result = exceptionGlobalHandler.handleOrderNotFound(exception);

        // Then
        assertThat(result.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
        assertThat(result.getTitle()).isEqualTo("Pedido não encontrado");
        assertThat(result.getDetail()).contains("Pedido não encontrado para o id:");
    }

    @Test
    void shouldHandleOrderNotFoundStatusException() {
        // Given
        OrderStatus status = OrderStatus.RECEIVED;
        OrderNotFoundStatusException exception = new OrderNotFoundStatusException(status);

        // When
        ProblemDetail result = exceptionGlobalHandler.handleOrderNotFoundStatus(exception);

        // Then
        assertThat(result.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
        assertThat(result.getTitle()).isEqualTo("Pedido não encontrado");
        assertThat(result.getDetail()).contains("Pedido não encontrado com o status:");
    }

    @Test
    void shouldHandleValidationException() {
        // Given
        ValidationException exception = new ValidationException("Validation error");

        // When
        ProblemDetail result = exceptionGlobalHandler.handleValidation(exception);

        // Then
        assertThat(result.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(result.getTitle()).isEqualTo("Erro de validação");
        assertThat(result.getDetail()).isEqualTo("Validation error");
    }

    @Test
    void shouldHandleInvalidUseCaseInputException() {
        // Given
        InvalidUseCaseInputException exception = new InvalidUseCaseInputException("Invalid input");

        // When
        ProblemDetail result = exceptionGlobalHandler.handleInvalidUseCaseInput(exception);

        // Then
        assertThat(result.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(result.getTitle()).isEqualTo("Entrada inválida");
        assertThat(result.getDetail()).isEqualTo("Invalid input");
    }

    @Test
    void shouldHandleUseCaseExecutionException() {
        // Given
        UseCaseExecutionException exception = new UseCaseExecutionException("Execution error");

        // When
        ProblemDetail result = exceptionGlobalHandler.handleUseCaseExecution(exception);

        // Then
        assertThat(result.getStatus()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
        assertThat(result.getTitle()).isEqualTo("Erro ao processar requisição");
        assertThat(result.getDetail()).isEqualTo("Execution error");
    }

    @Test
    void shouldHandleRepositoryException() {
        // Given
        RepositoryException exception = new RepositoryException("Repository error");

        // When
        ProblemDetail result = exceptionGlobalHandler.handleRepository(exception);

        // Then
        assertThat(result.getStatus()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
        assertThat(result.getTitle()).isEqualTo("Erro interno");
        assertThat(result.getDetail()).isEqualTo("Erro ao acessar recurso persistente");
    }

    @Test
    void shouldHandleGenericException() {
        // Given
        Exception exception = new Exception("Generic error");

        // When
        ProblemDetail result = exceptionGlobalHandler.handleGeneric(exception);

        // Then
        assertThat(result.getStatus()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
        assertThat(result.getTitle()).isEqualTo("Erro interno inesperado");
        assertThat(result.getDetail()).isEqualTo("Ocorreu um erro inesperado");
    }
}


