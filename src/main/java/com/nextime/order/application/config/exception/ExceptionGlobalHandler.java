package com.nextime.order.application.config.exception;

import com.nextime.order.domain.exception.ExceptionDetail;
import com.nextime.order.domain.exception.ValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionGlobalHandler {

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<?> handleException(ValidationException ex) {
        var details = new ExceptionDetail(HttpStatus.BAD_REQUEST.value(), ex.getMessage());
        return new ResponseEntity<>(details, HttpStatus.BAD_REQUEST);
    }
}
