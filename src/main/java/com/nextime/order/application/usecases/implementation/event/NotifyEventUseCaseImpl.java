package com.nextime.order.application.usecases.implementation.event;

import com.nextime.order.application.exception.InvalidEventException;
import com.nextime.order.application.gateways.EventRepositoryPort;
import com.nextime.order.application.usecases.interfaces.event.NotifyEventUseCase;
import com.nextime.order.infrastructure.persistence.document.Event;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class NotifyEventUseCaseImpl implements NotifyEventUseCase {

    private final EventRepositoryPort eventRepository;

    @Override
    public void execute(Event event) {
        validate(event);

        eventRepository.save(event);
    }

    private void validate(Event event) {
        if (event == null) {
            throw new InvalidEventException("Evento não pode ser nulo");
        }

        if (event.getOrderId() == null) {
            throw new InvalidEventException("orderId é obrigatório");
        }
    }
}
