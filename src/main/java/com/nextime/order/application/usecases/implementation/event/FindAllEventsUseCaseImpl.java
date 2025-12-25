package com.nextime.order.application.usecases.implementation.event;

import com.nextime.order.application.gateways.EventRepositoryPort;
import com.nextime.order.application.usecases.interfaces.event.FindAllEventsUseCase;
import com.nextime.order.domain.exception.event.EventNotFoundException;
import com.nextime.order.infrastructure.persistence.document.Event;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class FindAllEventsUseCaseImpl implements FindAllEventsUseCase {

    private final EventRepositoryPort eventRepository;

    @Override
    public List<Event> execute() {
        final List<Event> events = eventRepository.findAllOrderedByCreationDate();

        if (events.isEmpty()) {
            throw new EventNotFoundException();
        }

        return events;
    }
}
