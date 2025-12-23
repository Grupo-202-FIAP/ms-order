package com.nexTime.order.domain.exception.order;

import com.nexTime.order.domain.enums.OrderStatus;

public class OrderNotFoundStatusException extends RuntimeException {
    public OrderNotFoundStatusException(OrderStatus status) {
        super("[Exception] [Order] Pedido não encontrado com o status: " + status);
    }
}
