package com.nexTime.order.application.usecases.interfaces.event;

import com.nexTime.order.domain.EventFilters;
import com.nexTime.order.infrastructure.persistence.document.Event;

public interface FindEventByFiltersUseCase {
    Event execute(EventFilters filter);
}
