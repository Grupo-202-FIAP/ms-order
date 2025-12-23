package com.nexTime.msorder.infrastructure.adapters;

import com.nextime.order.application.gateways.LoggerPort;
import com.nextime.order.domain.OrderRequest;
import com.nextime.order.infrastructure.adapters.EventAdapter;
import com.nextime.order.infrastructure.adapters.OrderAdapter;
import com.nextime.order.infrastructure.messaging.producer.SagaProducer;
import com.nextime.order.infrastructure.persistence.document.Event;
import com.nextime.order.infrastructure.persistence.document.Order;
import com.nextime.order.infrastructure.persistence.document.OrderItem;
import com.nextime.order.infrastructure.persistence.document.Product;
import com.nextime.order.infrastructure.persistence.repository.IOrderRepository;
import com.nextime.order.utils.JsonConverter;
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
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes para OrderAdapter")
class OrderAdapterTest {

    @Mock
    private IOrderRepository orderRepository;

    @Mock
    private JsonConverter jsonConverter;

    @Mock
    private SagaProducer sagaProducer;

    @Mock
    private EventAdapter eventAdapter;

    @Mock
    private LoggerPort logger;

    @InjectMocks
    private OrderAdapter orderAdapter;

    private OrderRequest orderRequest;
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

        orderRequest = new OrderRequest(items);

        order = Order.builder()
                .id(UUID.randomUUID())
                .items(items)
                .transactionId(UUID.randomUUID())
                .build();
    }

    @Test
    @DisplayName("Deve criar um pedido com sucesso")
    void deveCriarPedidoComSucesso() {
        // Given
        when(orderRepository.save(any(Order.class))).thenReturn(order);
        when(jsonConverter.toJson(any(Event.class))).thenReturn("{\"id\":\"test\"}");
        when(eventAdapter.save(any(Event.class))).thenReturn(Event.builder().build());

        // When
        Order result = orderAdapter.createOrder(orderRequest);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(order.getId());
        assertThat(result.getTransactionId()).isNotNull();
        verify(orderRepository, times(1)).save(any(Order.class));
        verify(sagaProducer, times(1)).sendMessage(anyString());
        verify(eventAdapter, times(1)).save(any(Event.class));
    }

    @Test
    @DisplayName("Deve lançar exceção quando houver erro ao salvar pedido")
    void deveLancarExcecaoQuandoErroAoSalvar() {
        // Given
        when(orderRepository.save(any(Order.class))).thenThrow(new RuntimeException("Erro no banco"));

        // When & Then
        assertThatThrownBy(() -> orderAdapter.createOrder(orderRequest))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Erro ao criar pedido");

        verify(logger, times(1)).error(anyString(), any(Exception.class));
        verify(sagaProducer, never()).sendMessage(anyString());
        verify(eventAdapter, never()).save(any(Event.class));
    }

    @Test
    @DisplayName("Deve criar evento ao criar pedido")
    void deveCriarEventoAoCriarPedido() {
        // Given
        when(orderRepository.save(any(Order.class))).thenReturn(order);
        when(jsonConverter.toJson(any(Event.class))).thenReturn("{\"id\":\"test\"}");
        when(eventAdapter.save(any(Event.class))).thenReturn(Event.builder().build());

        // When
        orderAdapter.createOrder(orderRequest);

        // Then
        verify(eventAdapter, times(1)).save(any(Event.class));
        verify(sagaProducer, times(1)).sendMessage(anyString());
    }

    @Test
    @DisplayName("Deve gerar transactionId único para cada pedido")
    void deveGerarTransactionIdUnico() {
        // Given
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(jsonConverter.toJson(any(Event.class))).thenReturn("{\"id\":\"test\"}");
        when(eventAdapter.save(any(Event.class))).thenReturn(Event.builder().build());

        // When
        Order result1 = orderAdapter.createOrder(orderRequest);
        Order result2 = orderAdapter.createOrder(orderRequest);

        // Then
        assertThat(result1.getTransactionId()).isNotEqualTo(result2.getTransactionId());
    }
}

