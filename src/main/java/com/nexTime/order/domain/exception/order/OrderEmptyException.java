package com.nexTime.order.domain.exception.order;

public class OrderEmptyException extends RuntimeException {
    public OrderEmptyException() {
        super("[Exception] [Order] Nenhum pedido encontrado");
    }
}
