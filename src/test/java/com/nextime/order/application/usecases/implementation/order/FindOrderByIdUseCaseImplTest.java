package com.nextime.order.application.usecases.implementation.order;

import com.nextime.order.application.gateways.OrderRepositoryPort;
import com.nextime.order.domain.exception.order.OrderNotFoundException;
import com.nextime.order.infrastructure.persistence.document.Order;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Optional;
import java.util.UUID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FindOrderByIdUseCaseImplTest {

    @Mock
    private OrderRepositoryPort orderRepository;

    @InjectMocks
    private FindOrderByIdUseCaseImpl findOrderByIdUseCase;

    private UUID orderId;
    private Order order;

    @BeforeEach
    void setUp() {
        orderId = UUID.randomUUID();
        order = Order.builder()
                .id(orderId)
                .build();
    }

    @Test
    void shouldFindOrderByIdSuccessfully() {
        // Given
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

        // When
        Order result = findOrderByIdUseCase.execute(orderId);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(orderId);
    }

    @Test
    void shouldThrowOrderNotFoundExceptionWhenOrderNotFound() {
        // Given
        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

        // When/Then
        assertThatThrownBy(() -> findOrderByIdUseCase.execute(orderId))
                .isInstanceOf(OrderNotFoundException.class)
                .hasMessageContaining("Pedido não encontrado para o id: " + orderId);
    }
}

