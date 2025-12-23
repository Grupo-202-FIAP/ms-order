package com.nexTime.order.domain.exception.event;

import java.util.UUID;

public class EventNotFoundTransactionIdException extends RuntimeException {
    public EventNotFoundTransactionIdException(UUID transactionId) {
        super("[Exception] [Event] Não foi encontrado um evento com o transaction ID: " + transactionId);
    }
}
