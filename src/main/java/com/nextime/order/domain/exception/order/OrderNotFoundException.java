package com.nextime.order.domain.exception.order;

import java.util.UUID;

public class OrderNotFoundException extends RuntimeException {

    public OrderNotFoundException(UUID orderId) {
        super("Pedido não encontrado para o id: " + orderId);
    }
}
