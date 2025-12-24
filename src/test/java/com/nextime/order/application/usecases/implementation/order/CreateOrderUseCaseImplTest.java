package com.nextime.order.application.usecases.implementation.order;

import com.nextime.order.application.gateways.LoggerPort;
import com.nextime.order.application.usecases.implementation.event.SaveEventUseCaseImpl;
import com.nextime.order.application.usecases.implementation.order.CreateOrderUseCaseImpl;
import com.nextime.order.application.usecases.interfaces.event.SaveEventUseCase;
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

