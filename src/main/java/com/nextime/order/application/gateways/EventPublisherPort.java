package com.nextime.order.application.gateways;

import com.nextime.order.infrastructure.persistence.document.Order;

public interface EventPublisherPort {
    void publish(Order order);
}
