package com.nextime.order.application.usecases.implementation.event;

import com.nextime.order.application.usecases.interfaces.event.FindAllEventsUseCase;
import com.nextime.order.domain.exception.event.EventNotFoundException;
import com.nextime.order.infrastructure.persistence.document.Event;
import com.nextime.order.infrastructure.persistence.repository.IEventRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@AllArgsConstructor
public class FindAllEventsUseCaseImpl implements FindAllEventsUseCase {

    private final IEventRepository eventRepository;

    @Override
    public List<Event> execute() {
        final List<Event> events = eventRepository.findAllByOrderByCreatedAtDesc();

        if (events.isEmpty()) {
            throw new EventNotFoundException();
        }

        return events;
    }
}
