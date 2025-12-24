package com.nextime.order.application.usecases.implementation.order;

import com.nextime.order.application.gateways.LoggerPort;
import com.nextime.order.application.usecases.interfaces.event.SaveEventUseCase;
import com.nextime.order.application.usecases.interfaces.order.CreateOrderUseCase;
import com.nextime.order.domain.enums.OrderStatus;
import com.nextime.order.domain.exception.order.OrderListEmptyException;
import com.nextime.order.infrastructure.messaging.producer.SagaProducer;
import com.nextime.order.infrastructure.persistence.document.Event;
import com.nextime.order.infrastructure.persistence.document.Order;
import com.nextime.order.infrastructure.persistence.repository.IOrderRepository;
import com.nextime.order.utils.JsonConverter;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CreateOrderUseCaseImpl implements CreateOrderUseCase {

    private final IOrderRepository orderRepositoryPort;
    private final JsonConverter jsonConverter;
    private final SagaProducer sagaProducer;
    private final SaveEventUseCase saveEventUseCase;
    private final LoggerPort logger;

    @Override
    public Order execute(Order order) {

        if (order.getItems() == null || order.getItems().isEmpty()) {
            throw new OrderListEmptyException();
        }

        logger.info("[OrderAdapter.createOrder] Iniciando criação de pedido");

        order.setStatus(OrderStatus.RECEIVED);
        order.setCreatedAt(LocalDateTime.now());

        final Order savedOrder = orderRepositoryPort.save(order);

        logger.info(
                "[OrderAdapter.createOrder] Pedido criado com sucesso. ID: {}",
                savedOrder.getId()
        );

        final Event event = createPayload(savedOrder);
        sagaProducer.sendMessage(jsonConverter.toJson(event));

        return savedOrder;
    }

    private Event createPayload(Order order) {
        final var event = Event.builder()
                .orderId(order.getId())
                .transactionId(order.getTransactionId())
                .payload(order)
                .createdAt(LocalDateTime.now())
                .build();

        saveEventUseCase.execute(event);
        return event;
    }

}
