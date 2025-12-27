package com.nextime.order.domain.exception.order;

import com.nextime.order.domain.enums.OrderStatus;

public class OrderNotFoundStatusException extends RuntimeException {

    public OrderNotFoundStatusException(OrderStatus status) {
        super("Pedido não encontrado com o status: " + status);
    }
}
