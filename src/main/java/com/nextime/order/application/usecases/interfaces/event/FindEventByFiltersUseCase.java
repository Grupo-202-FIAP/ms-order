package com.nextime.order.application.usecases.interfaces.event;

import com.nextime.order.domain.EventFilters;
import com.nextime.order.infrastructure.persistence.document.Event;

public interface FindEventByFiltersUseCase {
    Event execute(EventFilters filter);
}
