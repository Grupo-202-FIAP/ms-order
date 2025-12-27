package com.nextime.order.application.usecases.implementation.order;

import com.nextime.order.application.gateways.OrderRepositoryPort;
import com.nextime.order.domain.enums.OrderStatus;
import com.nextime.order.domain.exception.order.OrderNotFoundStatusException;
import com.nextime.order.infrastructure.persistence.document.Order;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ListOrdersByStatusUseCaseImplTest {

    @Mock
    private OrderRepositoryPort orderRepository;

    @InjectMocks
    private ListOrdersByStatusUseCaseImpl listOrdersByStatusUseCase;

    private OrderStatus status;

    @BeforeEach
    void setUp() {
        status = OrderStatus.RECEIVED;
    }

    @Test
    void shouldListOrdersByStatusSuccessfully() {
        // Given
        Order order1 = Order.builder()
                .id(UUID.randomUUID())
                .status(OrderStatus.RECEIVED)
                .build();
        Order order2 = Order.builder()
                .id(UUID.randomUUID())
                .status(OrderStatus.RECEIVED)
                .build();
        List<Order> orders = Arrays.asList(order1, order2);
        when(orderRepository.findByStatus(status)).thenReturn(orders);

        // When
        List<Order> result = listOrdersByStatusUseCase.execute(status);

        // Then
        assertThat(result).isNotNull();
        assertThat(result).hasSize(2);
        assertThat(result).containsExactly(order1, order2);
    }

    @Test
    void shouldThrowOrderNotFoundStatusExceptionWhenNoOrdersFound() {
        // Given
        when(orderRepository.findByStatus(status)).thenReturn(new ArrayList<>());

        // When/Then
        assertThatThrownBy(() -> listOrdersByStatusUseCase.execute(status))
                .isInstanceOf(OrderNotFoundStatusException.class);
    }
}

