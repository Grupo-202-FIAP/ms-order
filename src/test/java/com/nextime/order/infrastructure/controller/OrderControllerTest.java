package com.nextime.order.infrastructure.controller;

import com.nextime.order.application.gateways.LoggerRepositoryPort;
import com.nextime.order.application.mapper.OrderMapper;
import com.nextime.order.application.usecases.interfaces.order.CreateOrderUseCase;
import com.nextime.order.application.usecases.interfaces.order.ListOrdersByStatusUseCase;
import com.nextime.order.application.usecases.interfaces.order.ListOrdersUseCase;
import com.nextime.order.domain.enums.OrderStatus;
import com.nextime.order.infrastructure.controller.dto.request.OrderRequest;
import com.nextime.order.infrastructure.controller.dto.response.OrderResponse;
import com.nextime.order.infrastructure.persistence.document.Order;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderControllerTest {

    @Mock
    private CreateOrderUseCase createOrderUseCase;

    @Mock
    private ListOrdersUseCase listOrdersUseCase;

    @Mock
    private ListOrdersByStatusUseCase listOrdersByStatusUseCase;

    @Mock
    private OrderMapper orderMapper;

    @Mock
    private LoggerRepositoryPort logger;

    @InjectMocks
    private OrderController orderController;

    private OrderRequest orderRequest;
    private Order order;
    private OrderResponse orderResponse;

    @BeforeEach
    void setUp() {
        UUID orderId = UUID.randomUUID();
        orderRequest = OrderRequest.builder()
                .customerId(UUID.randomUUID())
                .items(List.of())
                .build();

        order = Order.builder()
                .id(orderId)
                .status(OrderStatus.RECEIVED)
                .totalPrice(BigDecimal.valueOf(100.0))
                .build();

        orderResponse = OrderResponse.builder()
                .id(orderId)
                .status(OrderStatus.RECEIVED)
                .totalPrice(BigDecimal.valueOf(100.0))
                .build();
    }

    @Test
    void shouldCreateOrderSuccessfully() {
        // Given
        when(orderMapper.toDomain(orderRequest)).thenReturn(order);
        when(createOrderUseCase.execute(order)).thenReturn(order);
        when(orderMapper.toResponse(order)).thenReturn(orderResponse);

        // When
        ResponseEntity<OrderResponse> response = orderController.createOrder(orderRequest);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(orderResponse);
        verify(logger).info(any(String.class));
        verify(orderMapper).toDomain(orderRequest);
        verify(createOrderUseCase).execute(order);
        verify(orderMapper).toResponse(order);
    }

    @Test
    void shouldListOrdersSuccessfully() {
        // Given
        Order order1 = Order.builder().id(UUID.randomUUID()).build();
        Order order2 = Order.builder().id(UUID.randomUUID()).build();
        List<Order> orders = Arrays.asList(order1, order2);
        OrderResponse response1 = OrderResponse.builder().id(order1.getId()).build();
        OrderResponse response2 = OrderResponse.builder().id(order2.getId()).build();

        when(listOrdersUseCase.execute()).thenReturn(orders);
        when(orderMapper.toResponse(order1)).thenReturn(response1);
        when(orderMapper.toResponse(order2)).thenReturn(response2);

        // When
        ResponseEntity<List<OrderResponse>> response = orderController.listOrders();

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).hasSize(2);
        verify(logger).info(any(String.class));
        verify(listOrdersUseCase).execute();
    }

    @Test
    void shouldListOrdersByStatusSuccessfully() {
        // Given
        OrderStatus status = OrderStatus.RECEIVED;
        Order order1 = Order.builder()
                .id(UUID.randomUUID())
                .status(status)
                .build();
        Order order2 = Order.builder()
                .id(UUID.randomUUID())
                .status(status)
                .build();
        List<Order> orders = Arrays.asList(order1, order2);
        OrderResponse response1 = OrderResponse.builder().id(order1.getId()).build();
        OrderResponse response2 = OrderResponse.builder().id(order2.getId()).build();

        when(listOrdersByStatusUseCase.execute(status)).thenReturn(orders);
        when(orderMapper.toResponse(order1)).thenReturn(response1);
        when(orderMapper.toResponse(order2)).thenReturn(response2);

        // When
        ResponseEntity<List<OrderResponse>> response = orderController.listOrderByStatus(status);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).hasSize(2);
        verify(logger).info(any(String.class));
        verify(listOrdersByStatusUseCase).execute(status);
    }
}

