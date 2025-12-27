package com.nextime.order.domain.exception.order;

public class OrderListEmptyException extends RuntimeException {

    public OrderListEmptyException() {
        super("A lista de itens do pedido não pode ser vazia");
    }
}
