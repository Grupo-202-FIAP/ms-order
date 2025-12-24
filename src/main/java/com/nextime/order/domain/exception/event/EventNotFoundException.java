package com.nextime.order.domain.exception.event;

public class EventNotFoundException extends RuntimeException {
    public EventNotFoundException() {
        super("[Exception] [Event] Evento não encontrado ");
    }
}
