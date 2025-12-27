package com.nextime.order.infrastructure.controller.exception;

import com.nextime.order.application.exception.InvalidUseCaseInputException;
import com.nextime.order.application.exception.UseCaseExecutionException;
import com.nextime.order.application.exception.ValidationException;
import com.nextime.order.domain.exception.event.EventNotFoundException;
import com.nextime.order.domain.exception.event.EventNotFoundOrderIdException;
import com.nextime.order.domain.exception.event.EventNotFoundTransactionIdException;
import com.nextime.order.domain.exception.order.OrderEmptyException;
import com.nextime.order.domain.exception.order.OrderListEmptyException;
import com.nextime.order.domain.exception.order.OrderNotFoundException;
import com.nextime.order.domain.exception.order.OrderNotFoundStatusException;
import com.nextime.order.infrastructure.exception.RepositoryException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionGlobalHandler {

    private static final String EVENT_NOT_FOUND_TITLE = "Evento não encontrado";
    private static final String ORDER_NOT_FOUND_TITLE = "Pedido não encontrado";

    @ExceptionHandler(EventNotFoundException.class)
    public ProblemDetail handleEventNotFound(EventNotFoundException ex) {

        final ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.NOT_FOUND);
        problem.setTitle(EVENT_NOT_FOUND_TITLE);
        problem.setDetail(ex.getMessage());
        return problem;
    }

    @ExceptionHandler(EventNotFoundOrderIdException.class)
    public ProblemDetail handleEventNotFoundByOrderId(EventNotFoundOrderIdException ex) {

        final ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.NOT_FOUND);
        problem.setTitle(EVENT_NOT_FOUND_TITLE);
        problem.setDetail(ex.getMessage());
        return problem;
    }

    @ExceptionHandler(EventNotFoundTransactionIdException.class)
    public ProblemDetail handleEventNotFoundByTransactionId(EventNotFoundTransactionIdException ex) {

        final ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.NOT_FOUND);
        problem.setTitle(EVENT_NOT_FOUND_TITLE);
        problem.setDetail(ex.getMessage());
        return problem;
    }


    @ExceptionHandler(OrderEmptyException.class)
    public ProblemDetail handleOrderEmpty(OrderEmptyException ex) {

        final ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.NOT_FOUND);
        problem.setTitle(ORDER_NOT_FOUND_TITLE);
        problem.setDetail(ex.getMessage());
        return problem;
    }

    @ExceptionHandler(OrderListEmptyException.class)
    public ProblemDetail handleOrderListEmpty(OrderListEmptyException ex) {

        final ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        problem.setTitle("Lista de itens inválida");
        problem.setDetail(ex.getMessage());
        return problem;
    }

    @ExceptionHandler(OrderNotFoundException.class)
    public ProblemDetail handleOrderNotFound(OrderNotFoundException ex) {

        final ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.NOT_FOUND);
        problem.setTitle(ORDER_NOT_FOUND_TITLE);
        problem.setDetail(ex.getMessage());
        return problem;
    }

    @ExceptionHandler(OrderNotFoundStatusException.class)
    public ProblemDetail handleOrderNotFoundStatus(OrderNotFoundStatusException ex) {

        final ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.NOT_FOUND);
        problem.setTitle(ORDER_NOT_FOUND_TITLE);
        problem.setDetail(ex.getMessage());
        return problem;
    }


    @ExceptionHandler(ValidationException.class)
    public ProblemDetail handleValidation(ValidationException ex) {

        final ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        problem.setTitle("Erro de validação");
        problem.setDetail(ex.getMessage());
        return problem;
    }

    @ExceptionHandler(InvalidUseCaseInputException.class)
    public ProblemDetail handleInvalidUseCaseInput(InvalidUseCaseInputException ex) {

        final ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        problem.setTitle("Entrada inválida");
        problem.setDetail(ex.getMessage());
        return problem;
    }

    @ExceptionHandler(UseCaseExecutionException.class)
    public ProblemDetail handleUseCaseExecution(UseCaseExecutionException ex) {

        final ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        problem.setTitle("Erro ao processar requisição");
        problem.setDetail(ex.getMessage());
        return problem;
    }

    @ExceptionHandler(RepositoryException.class)
    public ProblemDetail handleRepository(RepositoryException ex) {

        final ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        problem.setTitle("Erro interno");
        problem.setDetail("Erro ao acessar recurso persistente");
        return problem;
    }

    @ExceptionHandler(Exception.class)
    public ProblemDetail handleGeneric(Exception ex) {

        final ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        problem.setTitle("Erro interno inesperado");
        problem.setDetail("Ocorreu um erro inesperado");
        return problem;
    }
}
