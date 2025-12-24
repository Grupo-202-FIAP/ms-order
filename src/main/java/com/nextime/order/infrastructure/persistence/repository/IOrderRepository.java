package com.nextime.order.infrastructure.persistence.repository;

import com.nextime.order.domain.enums.OrderStatus;
import com.nextime.order.infrastructure.persistence.document.Order;
import java.util.List;
import java.util.UUID;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface IOrderRepository extends MongoRepository<Order, UUID> {
    List<Order> findOrdersByStatus(OrderStatus status);
}
