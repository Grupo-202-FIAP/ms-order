package com.nextime.order.application.usecases.implementation.order;

import com.nextime.order.application.gateways.OrderRepositoryPort;
import com.nextime.order.domain.exception.order.OrderEmptyException;
import com.nextime.order.infrastructure.persistence.document.Order;
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
class ListOrdersUseCaseImplTest {

    @Mock
    private OrderRepositoryPort orderRepository;

    @InjectMocks
    private ListOrdersUseCaseImpl listOrdersUseCase;

    @Test
    void shouldListOrdersSuccessfully() {
        // Given
        Order order1 = Order.builder().id(UUID.randomUUID()).build();
        Order order2 = Order.builder().id(UUID.randomUUID()).build();
        List<Order> orders = Arrays.asList(order1, order2);
        when(orderRepository.findAll()).thenReturn(orders);

        // When
        List<Order> result = listOrdersUseCase.execute();

        // Then
        assertThat(result).isNotNull();
        assertThat(result).hasSize(2);
        assertThat(result).containsExactly(order1, order2);
    }

    @Test
    void shouldThrowOrderEmptyExceptionWhenNoOrdersFound() {
        // Given
        when(orderRepository.findAll()).thenReturn(new ArrayList<>());

        // When/Then
        assertThatThrownBy(() -> listOrdersUseCase.execute())
                .isInstanceOf(OrderEmptyException.class);
    }
}

