package com.nextime.order.domain.exception.order;

import com.nextime.order.domain.enums.OrderStatus;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

class OrderNotFoundStatusExceptionTest {

    @Test
    void shouldCreateOrderNotFoundStatusExceptionWithStatus() {
        // Given
        OrderStatus status = OrderStatus.RECEIVED;

        // When
        OrderNotFoundStatusException exception = new OrderNotFoundStatusException(status);

        // Then
        assertThat(exception.getMessage()).contains("Pedido não encontrado com o status:");
        assertThat(exception.getMessage()).contains(status.toString());
    }
}

