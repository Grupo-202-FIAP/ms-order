package com.nexTime.msorder.application.usecases.implementation.order;

import com.nextime.order.application.usecases.implementation.order.CreateOrderUseCaseImpl;
import com.nextime.order.domain.enums.OrderStatus;
import com.nextime.order.domain.exception.order.OrderListEmptyException;
import com.nextime.order.infrastructure.persistence.document.Order;
import com.nextime.order.infrastructure.persistence.document.OrderItem;
import com.nextime.order.infrastructure.persistence.document.Product;
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
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes para CreateOrderUseCaseImpl")
class CreateOrderUseCaseImplTest {

    @Mock
    private IOrderRepository orderRepositoryPort;

    @InjectMocks
    private CreateOrderUseCaseImpl createOrderUseCase;

    private Order order;
    private List<OrderItem> items;

    @BeforeEach
    void setUp() {
        items = new ArrayList<>();
        Product product = Product.builder()
                .id(1L)
                .name("Hambúrguer")
                .unitPrice(new BigDecimal("25.50"))
                .build();

        OrderItem item = OrderItem.builder()
                .id(UUID.randomUUID())
                .product(product)
                .quantity(2)
                .build();

        items.add(item);

        order = Order.builder()
                .id(UUID.randomUUID())
                .items(items)
                .build();
    }

    @Test
    @DisplayName("Deve criar um pedido com sucesso")
    void deveCriarPedidoComSucesso() {
        // Given
        when(orderRepositoryPort.save(any(Order.class))).thenAnswer(invocation -> {
            Order savedOrder = invocation.getArgument(0);
            return savedOrder;
        });

        // When
        Order result = createOrderUseCase.execute(order);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getStatus()).isEqualTo(OrderStatus.RECEIVED);
        assertThat(result.getTotalPrice()).isEqualByComparingTo(new BigDecimal("51.00"));
        assertThat(result.getIdentifier()).isNotNull();
        assertThat(result.getIdentifier()).startsWith("ORD-");
        verify(orderRepositoryPort, times(1)).save(any(Order.class));
    }

    @Test
    @DisplayName("Deve calcular o preço total corretamente")
    void deveCalcularPrecoTotalCorretamente() {
        // Given
        Product product1 = Product.builder()
                .id(1L)
                .name("Hambúrguer")
                .unitPrice(new BigDecimal("25.50"))
                .build();

        Product product2 = Product.builder()
                .id(2L)
                .name("Batata Frita")
                .unitPrice(new BigDecimal("15.00"))
                .build();

        OrderItem item1 = OrderItem.builder()
                .id(UUID.randomUUID())
                .product(product1)
                .quantity(2)
                .build();

        OrderItem item2 = OrderItem.builder()
                .id(UUID.randomUUID())
                .product(product2)
                .quantity(3)
                .build();

        order.setItems(List.of(item1, item2));

        when(orderRepositoryPort.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        Order result = createOrderUseCase.execute(order);

        // Then
        assertThat(result.getTotalPrice()).isEqualByComparingTo(new BigDecimal("96.00"));
    }

    @Test
    @DisplayName("Deve lançar exceção quando a lista de itens estiver vazia")
    void deveLancarExcecaoQuandoListaItensVazia() {
        // Given
        order.setItems(new ArrayList<>());

        // When & Then
        assertThatThrownBy(() -> createOrderUseCase.execute(order))
                .isInstanceOf(OrderListEmptyException.class)
                .hasMessageContaining("A lista de items não pode ser vazia");

        verify(orderRepositoryPort, never()).save(any(Order.class));
    }

    @Test
    @DisplayName("Deve lançar exceção quando a lista de itens for null")
    void deveLancarExcecaoQuandoListaItensNull() {
        // Given
        order.setItems(null);

        // When & Then
        assertThatThrownBy(() -> createOrderUseCase.execute(order))
                .isInstanceOf(OrderListEmptyException.class)
                .hasMessageContaining("A lista de items não pode ser vazia");

        verify(orderRepositoryPort, never()).save(any(Order.class));
    }

}

