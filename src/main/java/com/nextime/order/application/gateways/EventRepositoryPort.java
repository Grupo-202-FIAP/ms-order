package com.nextime.order.application.gateways;

import com.nextime.order.infrastructure.persistence.document.Event;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface EventRepositoryPort {
    List<Event> findAllOrderedByCreationDate();

    Optional<Event> findLatestByOrderId(UUID orderId);

    Optional<Event> findLatestByTransactionId(UUID transactionId);

    Event save(Event event);
}
