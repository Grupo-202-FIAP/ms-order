package com.nexTime.order.application.usecases.interfaces;


import com.nexTime.order.infrastructure.persistence.document.Order;

public interface CreateOrderUseCase {
    Order execute(Order order);
}
