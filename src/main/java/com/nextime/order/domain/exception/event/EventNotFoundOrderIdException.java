package com.nextime.order.domain.exception.event;

import java.util.UUID;

public class EventNotFoundOrderIdException extends RuntimeException {
    public EventNotFoundOrderIdException(UUID orderId) {
        super("[Exception] [Event] Não foi encontrado um evento com o order id:" + orderId);
    }
}
