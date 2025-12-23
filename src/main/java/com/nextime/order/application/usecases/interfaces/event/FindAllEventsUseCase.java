package com.nextime.order.application.usecases.interfaces.event;

import com.nextime.order.infrastructure.persistence.document.Event;

import java.util.List;

public interface FindAllEventsUseCase {
    List<Event> execute();
}
