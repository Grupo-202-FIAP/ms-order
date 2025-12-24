package com.nextime.order.application.usecases.implementation.order;

import com.nextime.order.application.usecases.interfaces.order.CreateOrderUseCase;
import com.nextime.order.domain.enums.OrderStatus;
import com.nextime.order.domain.exception.order.OrderListEmptyException;
import com.nextime.order.infrastructure.persistence.document.Order;
import com.nextime.order.infrastructure.persistence.repository.IOrderRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CreateOrderUseCaseImpl implements CreateOrderUseCase {

    private final IOrderRepository orderRepositoryPort;

    @Override
    public Order execute(Order order) {

        order.setTotalPrice(order.calculateTotalPrice());
        order.setIdentifier(order.generateOrderId());
        order.setStatus(OrderStatus.RECEIVED);

        if (order.getItems() == null || order.getItems().isEmpty()) {
            throw new OrderListEmptyException();
        }
        return orderRepositoryPort.save(order);
    }
}
