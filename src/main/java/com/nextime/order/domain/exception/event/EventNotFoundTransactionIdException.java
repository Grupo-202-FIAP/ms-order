package com.nextime.order.domain.exception.event;

import java.util.UUID;

public class EventNotFoundTransactionIdException extends RuntimeException {

    public EventNotFoundTransactionIdException(UUID transactionId) {
        super("Evento não encontrado para o transactionId: " + transactionId);
    }
}
