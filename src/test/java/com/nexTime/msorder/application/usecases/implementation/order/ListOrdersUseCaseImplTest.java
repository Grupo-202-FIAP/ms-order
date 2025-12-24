package com.nexTime.msorder.application.usecases.implementation.order;

import com.nextime.order.application.usecases.implementation.order.ListOrdersUseCaseImpl;
import com.nextime.order.domain.enums.OrderStatus;
import com.nextime.order.domain.exception.order.OrderEmptyException;
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
@DisplayName("Testes para ListOrdersUseCaseImpl")
class ListOrdersUseCaseImplTest {

    @Mock
    private IOrderRepository orderRepositoryPort;

    @InjectMocks
    private ListOrdersUseCaseImpl listOrdersUseCase;

    private List<Order> orders;

    @BeforeEach
    void setUp() {
        orders = new ArrayList<>();
    }

    @Test
    @DisplayName("Deve listar todos os pedidos com sucesso")
    void deveListarTodosPedidosComSucesso() {
        // Given
        Order order1 = Order.builder()
                .id(UUID.randomUUID())
                .identifier("ORD-1111-1111")
                .status(OrderStatus.RECEIVED)
                .totalPrice(new BigDecimal("50.00"))
                .build();

        Order order2 = Order.builder()
                .id(UUID.randomUUID())
                .identifier("ORD-2222-2222")
                .status(OrderStatus.PREPARING)
                .totalPrice(new BigDecimal("75.00"))
                .build();

        orders = Arrays.asList(order1, order2);
        when(orderRepositoryPort.findAll()).thenReturn(orders);

        // When
        List<Order> result = listOrdersUseCase.execute();

        // Then
        assertThat(result).isNotNull();
        assertThat(result).hasSize(2);
        assertThat(result).containsExactlyInAnyOrder(order1, order2);
        verify(orderRepositoryPort, times(1)).findAll();
    }

    @Test
    @DisplayName("Deve lançar exceção quando não houver pedidos")
    void deveLancarExcecaoQuandoNaoHouverPedidos() {
        // Given
        when(orderRepositoryPort.findAll()).thenReturn(new ArrayList<>());

        // When & Then
        assertThatThrownBy(() -> listOrdersUseCase.execute())
                .isInstanceOf(OrderEmptyException.class)
                .hasMessageContaining("Nenhum pedido encontrado");

        verify(orderRepositoryPort, times(1)).findAll();
    }

    @Test
    @DisplayName("Deve retornar lista vazia quando não houver pedidos no repositório")
    void deveRetornarListaVaziaQuandoNaoHouverPedidos() {
        // Given
        when(orderRepositoryPort.findAll()).thenReturn(new ArrayList<>());

        // When & Then
        assertThatThrownBy(() -> listOrdersUseCase.execute())
                .isInstanceOf(OrderEmptyException.class);

        verify(orderRepositoryPort, times(1)).findAll();
    }

    @Test
    @DisplayName("Deve listar múltiplos pedidos com diferentes status")
    void deveListarMultiplosPedidosComDiferentesStatus() {
        // Given
        Order order1 = Order.builder()
                .id(UUID.randomUUID())
                .status(OrderStatus.RECEIVED)
                .build();

        Order order2 = Order.builder()
                .id(UUID.randomUUID())
                .status(OrderStatus.PREPARING)
                .build();

        Order order3 = Order.builder()
                .id(UUID.randomUUID())
                .status(OrderStatus.COMPLETED)
                .build();

        orders = Arrays.asList(order1, order2, order3);
        when(orderRepositoryPort.findAll()).thenReturn(orders);

        // When
        List<Order> result = listOrdersUseCase.execute();

        // Then
        assertThat(result).hasSize(3);
        assertThat(result).extracting(Order::getStatus)
                .containsExactlyInAnyOrder(OrderStatus.RECEIVED, OrderStatus.PREPARING, OrderStatus.COMPLETED);
    }
}

