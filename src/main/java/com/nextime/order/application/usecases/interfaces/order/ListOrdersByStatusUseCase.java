package com.nextime.order.application.usecases.interfaces.order;

import com.nextime.order.domain.enums.OrderStatus;
import com.nextime.order.infrastructure.persistence.document.Order;
import java.util.List;

public interface ListOrdersByStatusUseCase {
    List<Order> execute(OrderStatus status);
}
