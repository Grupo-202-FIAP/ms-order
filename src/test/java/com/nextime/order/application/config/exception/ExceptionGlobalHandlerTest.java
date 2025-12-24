package com.nextime.order.application.config.exception;

import com.nextime.order.application.config.exception.ExceptionGlobalHandler;
import com.nextime.order.domain.exception.ExceptionDetail;
import com.nextime.order.domain.exception.ValidationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Testes para ExceptionGlobalHandler")
class ExceptionGlobalHandlerTest {

    private ExceptionGlobalHandler exceptionGlobalHandler;

    @org.junit.jupiter.api.BeforeEach
    void setUp() {
        exceptionGlobalHandler = new ExceptionGlobalHandler();
    }

    @Test
    @DisplayName("Deve tratar ValidationException corretamente")
    void deveTratarValidationExceptionCorretamente() {
        // Given
        String errorMessage = "Erro de validação";
        ValidationException exception = new ValidationException(errorMessage);

        // When
        ResponseEntity<?> response = exceptionGlobalHandler.handleException(exception);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isInstanceOf(ExceptionDetail.class);
        
        ExceptionDetail detail = (ExceptionDetail) response.getBody();
        assertThat(detail).isNotNull();
        assertThat(detail.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(detail.message()).isEqualTo(errorMessage);
    }

    @Test
    @DisplayName("Deve retornar status 400 para ValidationException")
    void deveRetornarStatus400ParaValidationException() {
        // Given
        ValidationException exception = new ValidationException("Mensagem de erro");

        // When
        ResponseEntity<?> response = exceptionGlobalHandler.handleException(exception);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    @DisplayName("Deve incluir mensagem da exceção no ExceptionDetail")
    void deveIncluirMensagemExcecaoNoExceptionDetail() {
        // Given
        String customMessage = "Campo obrigatório não informado";
        ValidationException exception = new ValidationException(customMessage);

        // When
        ResponseEntity<?> response = exceptionGlobalHandler.handleException(exception);

        // Then
        ExceptionDetail detail = (ExceptionDetail) response.getBody();
        assertThat(detail.message()).isEqualTo(customMessage);
    }
}

