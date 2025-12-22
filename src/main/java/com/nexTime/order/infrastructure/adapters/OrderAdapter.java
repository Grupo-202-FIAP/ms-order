package com.nexTime.order.infrastructure.adapters;

import com.nexTime.order.application.gateways.LoggerPort;
import com.nexTime.order.application.gateways.OrderPort;
import com.nexTime.order.domain.OrderRequest;
import com.nexTime.order.infrastructure.messaging.producer.SagaProducer;
import com.nexTime.order.infrastructure.persistence.document.Event;
import com.nexTime.order.infrastructure.persistence.document.Order;
import com.nexTime.order.infrastructure.persistence.repository.IOrderRepository;
import com.nexTime.order.utils.JsonConverter;
import lombok.AllArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@AllArgsConstructor
public class OrderAdapter implements OrderPort {

    private final IOrderRepository orderRepository;
    private final JsonConverter jsonConverter;
    private final SagaProducer sagaProducer;
    private final EventAdapter eventAdapter;
    private final LoggerPort logger;


    public Order createOrder(OrderRequest orderRequest) {
        logger.info("[OrderAdapter.createOrder] Iniciando criação de pedido");
        var order = Order.builder()
                .items(orderRequest.getOrderItems())
                .createdAt(LocalDateTime.now())
                .transactionId(UUID.randomUUID())
                .build();
        try {
            val savedOrder = orderRepository.save(order);
            logger.info("[OrderAdapter.createOrder] Pedido criado com sucesso. ID: {}", savedOrder.getId());
            Event event = createPayload(savedOrder);
            sagaProducer.sendMessage(jsonConverter.toJson(event));
            return savedOrder;
        } catch (Exception e) {
            logger.error("[OrderAdapter.createOrder] Erro ao criar pedido: {}", e);
            throw new RuntimeException("Erro ao criar pedido", e);
        }
    }

    private Event createPayload(Order order) {
        var event = Event.builder()
                .orderId(order.getId())
                .transactionId(order.getTransactionId())
                .payload(order)
                .createdAt(LocalDateTime.now())
                .build();

        eventAdapter.save(event);
        return event;
    }
}
