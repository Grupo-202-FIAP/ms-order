package com.nexTime.msorder.application.usecases.implementation.order;

import com.nextime.order.application.usecases.implementation.order.FindOrderByIdUseCaseImpl;
import com.nextime.order.domain.enums.OrderStatus;
import com.nextime.order.domain.exception.order.OrderNotFoundException;
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
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes para FindOrderByIdUseCaseImpl")
class FindOrderByIdUseCaseImplTest {

    @Mock
    private IOrderRepository orderRepositoryPort;

    @InjectMocks
    private FindOrderByIdUseCaseImpl findOrderByIdUseCase;

    private UUID orderId;
    private Order order;

    @BeforeEach
    void setUp() {
        orderId = UUID.randomUUID();
        order = Order.builder()
                .id(orderId)
                .identifier("ORD-1234-5678")
                .totalPrice(new BigDecimal("50.00"))
                .status(OrderStatus.RECEIVED)
                .build();
    }

    @Test
    @DisplayName("Deve encontrar um pedido por ID com sucesso")
    void deveEncontrarPedidoPorIdComSucesso() {
        // Given
        when(orderRepositoryPort.findById(orderId)).thenReturn(Optional.of(order));

        // When
        Order result = findOrderByIdUseCase.execute(orderId);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(orderId);
        assertThat(result.getIdentifier()).isEqualTo("ORD-1234-5678");
        assertThat(result.getStatus()).isEqualTo(OrderStatus.RECEIVED);
        verify(orderRepositoryPort, times(1)).findById(orderId);
    }

    @Test
    @DisplayName("Deve lançar exceção quando pedido não for encontrado")
    void deveLancarExcecaoQuandoPedidoNaoEncontrado() {
        // Given
        when(orderRepositoryPort.findById(orderId)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> findOrderByIdUseCase.execute(orderId))
                .isInstanceOf(OrderNotFoundException.class)
                .hasMessageContaining("Pedido não encontrado: " + orderId);

        verify(orderRepositoryPort, times(1)).findById(orderId);
    }

    @Test
    @DisplayName("Deve buscar pedido com ID diferente")
    void deveBuscarPedidoComIdDiferente() {
        // Given
        UUID differentId = UUID.randomUUID();
        Order differentOrder = Order.builder()
                .id(differentId)
                .identifier("ORD-9999-8888")
                .build();

        when(orderRepositoryPort.findById(differentId)).thenReturn(Optional.of(differentOrder));

        // When
        Order result = findOrderByIdUseCase.execute(differentId);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(differentId);
        assertThat(result.getIdentifier()).isEqualTo("ORD-9999-8888");
        verify(orderRepositoryPort, times(1)).findById(differentId);
    }
}

