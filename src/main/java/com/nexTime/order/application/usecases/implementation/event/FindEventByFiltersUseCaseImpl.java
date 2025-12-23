package com.nexTime.order.application.usecases.implementation.event;

import com.nexTime.order.application.usecases.interfaces.event.FindEventByFiltersUseCase;
import com.nexTime.order.domain.EventFilters;
import com.nexTime.order.domain.exception.event.EventNotFoundOrderIdException;
import com.nexTime.order.domain.exception.event.EventNotFoundTransactionIdException;
import com.nexTime.order.infrastructure.persistence.document.Event;
import com.nexTime.order.infrastructure.persistence.repository.IEventRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;


import java.util.UUID;

import static org.springframework.util.ObjectUtils.isEmpty;

@Service
@AllArgsConstructor
public class FindEventByFiltersUseCaseImpl implements FindEventByFiltersUseCase {
    private final IEventRepository eventRepository;

    @Override
    public Event execute(EventFilters filter) {
        validateEmptyFilters(filter);

        if (!isEmpty(filter.getOrderId())) {
            return findByOrderId(filter.getOrderId());
        } else {
            return findByTransactionId(filter.getTransactionId());
        }
    }

    private Event findByOrderId(UUID orderId) {
        return eventRepository.findTop1ByOrderIdOrderByCreatedAtDesc(orderId)
                .orElseThrow(() -> new EventNotFoundOrderIdException(orderId));
    }

    private Event findByTransactionId(UUID transactionId) {
        return eventRepository.findTop1ByTransactionIdOrderByCreatedAtDesc(transactionId)
                .orElseThrow(() -> new EventNotFoundTransactionIdException(transactionId));
    }


    private void validateEmptyFilters(EventFilters filters) {
        if (isEmpty(filters.getOrderId()) && isEmpty(filters.getTransactionId())) {
            throw new IllegalArgumentException("[Exception] [Event] Filtros inválidos: é necessário fornecer um orderId ou transactionId para buscar o evento.");
        }
    }

}
