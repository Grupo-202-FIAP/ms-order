package com.nextime.order.infrastructure.adapters;

import com.nextime.order.application.gateways.LoggerRepositoryPort;
import com.nextime.order.domain.enums.OrderStatus;
import com.nextime.order.infrastructure.exception.RepositoryException;
import com.nextime.order.infrastructure.persistence.document.Order;
import com.nextime.order.infrastructure.persistence.repository.IOrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataAccessException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderRepositoryAdapterTest {

    @Mock
    private IOrderRepository orderRepository;

    @Mock
    private LoggerRepositoryPort logger;

    @InjectMocks
    private OrderRepositoryAdapter orderRepositoryAdapter;

    private Order order;
    private UUID orderId;

    @BeforeEach
    void setUp() {
        orderId = UUID.randomUUID();
        order = Order.builder()
                .id(orderId)
                .status(OrderStatus.RECEIVED)
                .build();
    }

    @Test
    void shouldSaveOrderSuccessfully() {
        // Given
        when(orderRepository.save(order)).thenReturn(order);

        // When
        Order result = orderRepositoryAdapter.save(order);

        // Then
        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(order);
        verify(logger).info(any(String.class), any(UUID.class));
        verify(orderRepository).save(order);
    }

    @Test
    void shouldThrowRepositoryExceptionWhenSaveFails() {
        // Given
        DataAccessException exception = new DataAccessException("Database error") {};
        when(orderRepository.save(order)).thenThrow(exception);

        // When/Then
        assertThatThrownBy(() -> orderRepositoryAdapter.save(order))
                .isInstanceOf(RepositoryException.class)
                .hasMessageContaining("Erro ao salvar pedido")
                .hasCause(exception);

        verify(logger).error(any(String.class), any(UUID.class), any(String.class));
    }

    @Test
    void shouldFindOrderByIdSuccessfully() {
        // Given
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

        // When
        Optional<Order> result = orderRepositoryAdapter.findById(orderId);

        // Then
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(order);
        verify(logger).info(any(String.class), any(UUID.class));
        verify(orderRepository).findById(orderId);
    }

    @Test
    void shouldReturnEmptyWhenOrderNotFound() {
        // Given
        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

        // When
        Optional<Order> result = orderRepositoryAdapter.findById(orderId);

        // Then
        assertThat(result).isEmpty();
        verify(logger).info(any(String.class), any(UUID.class));
    }

    @Test
    void shouldFindOrdersByStatusSuccessfully() {
        // Given
        OrderStatus status = OrderStatus.RECEIVED;
        Order order1 = Order.builder().id(UUID.randomUUID()).status(status).build();
        Order order2 = Order.builder().id(UUID.randomUUID()).status(status).build();
        List<Order> orders = Arrays.asList(order1, order2);
        when(orderRepository.findOrdersByStatus(status)).thenReturn(orders);

        // When
        List<Order> result = orderRepositoryAdapter.findByStatus(status);

        // Then
        assertThat(result).hasSize(2);
        assertThat(result).containsExactly(order1, order2);
        verify(logger).info(any(String.class), any(OrderStatus.class));
        verify(orderRepository).findOrdersByStatus(status);
    }

    @Test
    void shouldFindAllOrdersSuccessfully() {
        // Given
        Order order1 = Order.builder().id(UUID.randomUUID()).build();
        Order order2 = Order.builder().id(UUID.randomUUID()).build();
        List<Order> orders = Arrays.asList(order1, order2);
        when(orderRepository.findAll()).thenReturn(orders);

        // When
        List<Order> result = orderRepositoryAdapter.findAll();

        // Then
        assertThat(result).hasSize(2);
        assertThat(result).containsExactly(order1, order2);
        verify(logger).info(any(String.class));
        verify(orderRepository).findAll();
    }
}

