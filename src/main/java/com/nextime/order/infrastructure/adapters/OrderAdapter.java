package com.nextime.order.infrastructure.adapters;

import com.nextime.order.application.gateways.LoggerPort;
import com.nextime.order.application.gateways.OrderPort;
import com.nextime.order.domain.OrderRequest;
import com.nextime.order.infrastructure.messaging.producer.SagaProducer;
import com.nextime.order.infrastructure.persistence.document.Event;
import com.nextime.order.infrastructure.persistence.document.Order;
import com.nextime.order.infrastructure.persistence.repository.IOrderRepository;
import com.nextime.order.utils.JsonConverter;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@AllArgsConstructor
public class OrderAdapter implements OrderPort {

    private final IOrderRepository orderRepository;
    private final JsonConverter jsonConverter;
    private final SagaProducer sagaProducer;
    private final EventAdapter eventAdapter;
    private final LoggerPort logger;


    public Order createOrder(OrderRequest orderRequest) {
        logger.info("[OrderAdapter.createOrder] Iniciando criação de pedido");
        final var order = Order.builder()
                .items(orderRequest.getOrderItems())
                .createdAt(LocalDateTime.now())
                .transactionId(UUID.randomUUID())
                .build();
        try {
            final var savedOrder = orderRepository.save(order);
            logger.info("[OrderAdapter.createOrder] Pedido criado com sucesso. ID: {}", savedOrder.getId());
            final Event event = createPayload(savedOrder);
            sagaProducer.sendMessage(jsonConverter.toJson(event));
            return savedOrder;
        } catch (Exception e) {
            logger.error("[OrderAdapter.createOrder] Erro ao criar pedido: {}", e);
            throw new RuntimeException("Erro ao criar pedido", e);
        }
    }

    private Event createPayload(Order order) {
        final var event = Event.builder()
                .orderId(order.getId())
                .transactionId(order.getTransactionId())
                .payload(order)
                .createdAt(LocalDateTime.now())
                .build();

        eventAdapter.save(event);
        return event;
    }
}
