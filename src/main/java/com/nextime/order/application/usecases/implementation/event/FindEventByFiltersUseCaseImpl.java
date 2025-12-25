package com.nextime.order.application.usecases.implementation.event;

import com.nextime.order.application.exception.InvalidEventFilterException;
import com.nextime.order.application.gateways.EventRepositoryPort;
import com.nextime.order.application.usecases.interfaces.event.FindEventByFiltersUseCase;
import com.nextime.order.domain.EventFilters;
import com.nextime.order.domain.exception.event.EventNotFoundOrderIdException;
import com.nextime.order.domain.exception.event.EventNotFoundTransactionIdException;
import com.nextime.order.infrastructure.persistence.document.Event;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class FindEventByFiltersUseCaseImpl
        implements FindEventByFiltersUseCase {

    private final EventRepositoryPort eventRepository;

    @Override
    public Event execute(EventFilters filters) {
        validateFilters(filters);

        if (filters.hasOrderId()) {
            return findLatestByOrderId(filters.getOrderId());
        }

        return findLatestByTransactionId(filters.getTransactionId());
    }

    private Event findLatestByOrderId(UUID orderId) {
        return eventRepository.findLatestByOrderId(orderId)
                .orElseThrow(() -> new EventNotFoundOrderIdException(orderId));
    }

    private Event findLatestByTransactionId(UUID transactionId) {
        return eventRepository.findLatestByTransactionId(transactionId)
                .orElseThrow(() -> new EventNotFoundTransactionIdException(transactionId));
    }

    private void validateFilters(EventFilters filters) {
        if (filters == null || filters.isEmpty()) {
            throw new InvalidEventFilterException(
                    "É necessário informar orderId ou transactionId");
        }
    }
}
