package com.nextime.order.application.usecases.interfaces.event;

import com.nextime.order.infrastructure.persistence.document.Event;

public interface SaveEventUseCase {
    Event execute(Event event);
}
