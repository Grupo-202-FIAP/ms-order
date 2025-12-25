package com.nextime.order.application.usecases.implementation.order;

import com.nextime.order.application.gateways.EventPublisherPort;
import com.nextime.order.application.gateways.OrderRepositoryPort;
import com.nextime.order.application.usecases.interfaces.order.CreateOrderUseCase;
import com.nextime.order.domain.enums.PaymentStatus;
import com.nextime.order.domain.exception.order.OrderListEmptyException;
import com.nextime.order.infrastructure.persistence.document.Order;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CreateOrderUseCaseImpl implements CreateOrderUseCase {

    private final OrderRepositoryPort orderRepository;
    private final EventPublisherPort eventPublisher;

    @Override
    public Order execute(Order order) {

        if (order.getItems() == null || order.getItems().isEmpty()) {
            throw new OrderListEmptyException();
        }

        order.setPaymentStatus(PaymentStatus.UNKNOWN);

        final Order savedOrder = orderRepository.save(order);

        eventPublisher.publish(savedOrder);

        return savedOrder;
    }
}
