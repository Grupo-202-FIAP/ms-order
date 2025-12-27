package com.nextime.order.application.usecases.interfaces.order;


import com.nextime.order.infrastructure.persistence.document.Order;

public interface CreateOrderUseCase {
    Order execute(Order order);
}
