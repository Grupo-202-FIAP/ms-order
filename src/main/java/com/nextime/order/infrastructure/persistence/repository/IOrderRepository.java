package com.nextime.order.infrastructure.persistence.repository;

import com.nextime.order.domain.enums.OrderStatus;
import com.nextime.order.infrastructure.persistence.document.Order;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;
import java.util.UUID;

public interface IOrderRepository extends MongoRepository<Order, UUID> {
    List<Order> findOrdersByStatus(OrderStatus status);
}
