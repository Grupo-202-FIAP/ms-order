package com.nexTime.order.infrastructure.persistence.repository;

import com.nexTime.order.infrastructure.persistence.document.Order;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.UUID;

public interface IOrderRepository extends MongoRepository<Order, UUID> {
}
