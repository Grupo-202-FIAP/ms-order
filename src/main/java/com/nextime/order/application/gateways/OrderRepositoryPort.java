package com.nextime.order.application.gateways;

import com.nextime.order.domain.enums.OrderStatus;
import com.nextime.order.infrastructure.persistence.document.Order;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OrderRepositoryPort {
    Order save(Order order);

    Optional<Order> findById(UUID orderId);

    List<Order> findByStatus(OrderStatus status);

    List<Order> findAll();
}
