package com.nextime.order.application.usecases.interfaces.event;

import com.nextime.order.infrastructure.persistence.document.Event;

public interface NotifyEventUseCase {
    void execute(Event event);
}
