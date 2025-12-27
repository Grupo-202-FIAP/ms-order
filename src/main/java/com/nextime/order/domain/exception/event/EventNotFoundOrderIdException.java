package com.nextime.order.domain.exception.event;

import java.util.UUID;

public class EventNotFoundOrderIdException extends RuntimeException {

    public EventNotFoundOrderIdException(UUID orderId) {
        super("Evento não encontrado para o orderId: " + orderId);
    }
}
