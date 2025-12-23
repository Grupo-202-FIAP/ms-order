package com.nextime.order.infrastructure.persistence.repository;

import com.nextime.order.infrastructure.persistence.document.Event;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IEventRepository extends MongoRepository<Event, UUID> {
    List<Event> findAllByOrderByCreatedAtDesc();

    Optional<Event> findTop1ByOrderIdOrderByCreatedAtDesc(UUID orderId);

    Optional<Event> findTop1ByTransactionIdOrderByCreatedAtDesc(UUID transactionId);
}
