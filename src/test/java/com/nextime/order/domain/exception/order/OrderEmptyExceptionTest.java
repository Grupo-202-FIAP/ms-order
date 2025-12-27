package com.nextime.order.domain.exception.order;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

class OrderEmptyExceptionTest {

    @Test
    void shouldCreateOrderEmptyException() {
        // When
        OrderEmptyException exception = new OrderEmptyException();

        // Then
        assertThat(exception.getMessage()).isEqualTo("Nenhum pedido encontrado");
    }
}

