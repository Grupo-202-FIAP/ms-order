package com.nexTime.order.infrastructure.persistence.repository;

import com.nexTime.order.infrastructure.persistence.document.Event;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.UUID;

public interface IEventRepository extends MongoRepository<Event, UUID> {
}
