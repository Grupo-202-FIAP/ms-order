package com.nextime.order.application.usecases.implementation.order;

import com.nextime.order.application.usecases.implementation.order.ListOrdersByStatusUseCaseImpl;
import com.nextime.order.domain.enums.OrderStatus;
import com.nextime.order.domain.exception.order.OrderNotFoundStatusException;
import com.nextime.order.infrastructure.persistence.document.Order;
import com.nextime.order.infrastructure.persistence.repository.IOrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes para ListOrdersByStatusUseCaseImpl")
class ListOrdersByStatusUseCaseImplTest {

    @Mock
    private IOrderRepository orderRepositoryPort;

    @InjectMocks
    private ListOrdersByStatusUseCaseImpl listOrdersByStatusUseCase;

    private List<Order> orders;

    @BeforeEach
    void setUp() {
        orders = new ArrayList<>();
    }

    @Test
    @DisplayName("Deve listar pedidos por status com sucesso")
    void deveListarPedidosPorStatusComSucesso() {
        // Given
        OrderStatus status = OrderStatus.RECEIVED;
        Order order1 = Order.builder()
                .id(UUID.randomUUID())
                .identifier("ORD-1111-1111")
                .status(OrderStatus.RECEIVED)
                .totalPrice(new BigDecimal("50.00"))
                .build();

        Order order2 = Order.builder()
                .id(UUID.randomUUID())
                .identifier("ORD-2222-2222")
                .status(OrderStatus.RECEIVED)
                .totalPrice(new BigDecimal("75.00"))
                .build();

        orders = Arrays.asList(order1, order2);
        when(orderRepositoryPort.findOrdersByStatus(status)).thenReturn(orders);

        // When
        List<Order> result = listOrdersByStatusUseCase.execute(status);

        // Then
        assertThat(result).isNotNull();
        assertThat(result).hasSize(2);
        assertThat(result).allMatch(order -> order.getStatus() == OrderStatus.RECEIVED);
        verify(orderRepositoryPort, times(1)).findOrdersByStatus(status);
    }

    @Test
    @DisplayName("Deve lançar exceção quando não houver pedidos com o status especificado")
    void deveLancarExcecaoQuandoNaoHouverPedidosComStatus() {
        // Given
        OrderStatus status = OrderStatus.CANCELLED;
        when(orderRepositoryPort.findOrdersByStatus(status)).thenReturn(new ArrayList<>());

        // When & Then
        assertThatThrownBy(() -> listOrdersByStatusUseCase.execute(status))
                .isInstanceOf(OrderNotFoundStatusException.class)
                .hasMessageContaining("Pedido não encontrado com o status: " + status);

        verify(orderRepositoryPort, times(1)).findOrdersByStatus(status);
    }

    @Test
    @DisplayName("Deve listar pedidos com status PREPARING")
    void deveListarPedidosComStatusPreparing() {
        // Given
        OrderStatus status = OrderStatus.PREPARING;
        Order order = Order.builder()
                .id(UUID.randomUUID())
                .status(OrderStatus.PREPARING)
                .build();

        orders = Arrays.asList(order);
        when(orderRepositoryPort.findOrdersByStatus(status)).thenReturn(orders);

        // When
        List<Order> result = listOrdersByStatusUseCase.execute(status);

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getStatus()).isEqualTo(OrderStatus.PREPARING);
    }

    @Test
    @DisplayName("Deve listar pedidos com status READY")
    void deveListarPedidosComStatusReady() {
        // Given
        OrderStatus status = OrderStatus.READY;
        Order order1 = Order.builder()
                .id(UUID.randomUUID())
                .status(OrderStatus.READY)
                .build();

        Order order2 = Order.builder()
                .id(UUID.randomUUID())
                .status(OrderStatus.READY)
                .build();

        orders = Arrays.asList(order1, order2);
        when(orderRepositoryPort.findOrdersByStatus(status)).thenReturn(orders);

        // When
        List<Order> result = listOrdersByStatusUseCase.execute(status);

        // Then
        assertThat(result).hasSize(2);
        assertThat(result).allMatch(order -> order.getStatus() == OrderStatus.READY);
    }

    @Test
    @DisplayName("Deve listar pedidos com status COMPLETED")
    void deveListarPedidosComStatusCompleted() {
        // Given
        OrderStatus status = OrderStatus.COMPLETED;
        Order order = Order.builder()
                .id(UUID.randomUUID())
                .status(OrderStatus.COMPLETED)
                .build();

        orders = Arrays.asList(order);
        when(orderRepositoryPort.findOrdersByStatus(status)).thenReturn(orders);

        // When
        List<Order> result = listOrdersByStatusUseCase.execute(status);

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getStatus()).isEqualTo(OrderStatus.COMPLETED);
    }
}

