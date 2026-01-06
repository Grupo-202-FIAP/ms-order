package com.nextime.order.domain.exception.order;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

class OrderListEmptyExceptionTest {

    @Test
    void shouldCreateOrderListEmptyException() {
        // When
        OrderListEmptyException exception = new OrderListEmptyException();

        // Then
        assertThat(exception.getMessage()).isEqualTo("A lista de itens do pedido não pode ser vazia");
    }
}



