package com.nextime.order.application.usecases.implementation.order;

import com.nextime.order.application.gateways.EventPublisherPort;
import com.nextime.order.application.gateways.OrderRepositoryPort;
import com.nextime.order.domain.enums.PaymentStatus;
import com.nextime.order.domain.exception.order.OrderListEmptyException;
import com.nextime.order.infrastructure.persistence.document.Order;
import com.nextime.order.infrastructure.persistence.document.OrderItem;
import com.nextime.order.infrastructure.persistence.document.Product;
import org.junit.jupiter.api.BeforeEach;
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
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreateOrderUseCaseImplTest {

    @Mock
    private OrderRepositoryPort orderRepository;

    @Mock
    private EventPublisherPort eventPublisher;

    @InjectMocks
    private CreateOrderUseCaseImpl createOrderUseCase;

    private Order order;
    private Order savedOrder;

    @BeforeEach
    void setUp() {
        UUID orderId = UUID.randomUUID();
        List<OrderItem> items = new ArrayList<>();
        Product product = Product.builder()
                .id(1L)
                .name("Product 1")
                .unitPrice(BigDecimal.valueOf(10.0))
                .build();
        OrderItem item = OrderItem.builder()
                .id(UUID.randomUUID())
                .product(product)
                .quantity(2)
                .build();
        items.add(item);

        order = Order.builder()
                .id(orderId)
                .items(items)
                .build();

        savedOrder = Order.builder()
                .id(orderId)
                .items(items)
                .paymentStatus(PaymentStatus.PENDING)
                .build();
    }

    @Test
    void shouldCreateOrderSuccessfully() {
        // Given
        when(orderRepository.save(any(Order.class))).thenReturn(savedOrder);

        // When
        Order result = createOrderUseCase.execute(order);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getPaymentStatus()).isEqualTo(PaymentStatus.PENDING);
        verify(orderRepository).save(order);
        verify(eventPublisher).publish(savedOrder);
    }

    @Test
    void shouldThrowOrderListEmptyExceptionWhenItemsIsNull() {
        // Given
        order.setItems(null);

        // When/Then
        assertThatThrownBy(() -> createOrderUseCase.execute(order))
                .isInstanceOf(OrderListEmptyException.class);

        verify(orderRepository, never()).save(any());
        verify(eventPublisher, never()).publish(any());
    }

    @Test
    void shouldThrowOrderListEmptyExceptionWhenItemsIsEmpty() {
        // Given
        order.setItems(new ArrayList<>());

        // When/Then
        assertThatThrownBy(() -> createOrderUseCase.execute(order))
                .isInstanceOf(OrderListEmptyException.class);

        verify(orderRepository, never()).save(any());
        verify(eventPublisher, never()).publish(any());
    }
}

