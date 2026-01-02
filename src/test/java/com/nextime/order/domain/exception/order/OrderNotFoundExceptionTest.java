package com.nextime.order.domain.exception.order;

import org.junit.jupiter.api.Test;
import java.util.UUID;
import static org.assertj.core.api.Assertions.assertThat;

class OrderNotFoundExceptionTest {

    @Test
    void shouldCreateOrderNotFoundExceptionWithOrderId() {
        // Given
        UUID orderId = UUID.randomUUID();

        // When
        OrderNotFoundException exception = new OrderNotFoundException(orderId);

        // Then
        assertThat(exception.getMessage()).contains("Pedido não encontrado para o id:");
        assertThat(exception.getMessage()).contains(orderId.toString());
    }
}



