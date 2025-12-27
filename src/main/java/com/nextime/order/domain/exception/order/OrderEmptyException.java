package com.nextime.order.domain.exception.order;

public class OrderEmptyException extends RuntimeException {

    public OrderEmptyException() {
        super("Nenhum pedido encontrado");
    }
}
