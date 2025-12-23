package com.nextime.order.application.usecases.implementation.order;

import com.nextime.order.application.usecases.interfaces.order.ListOrdersByStatusUseCase;
import com.nextime.order.domain.enums.OrderStatus;
import com.nextime.order.domain.exception.order.OrderNotFoundStatusException;
import com.nextime.order.infrastructure.persistence.document.Order;
import com.nextime.order.infrastructure.persistence.repository.IOrderRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ListOrdersByStatusUseCaseImpl implements ListOrdersByStatusUseCase {
    private final IOrderRepository orderRepositoryPort;

    public List<Order> execute(OrderStatus status) {
        List<Order> orders = orderRepositoryPort.findOrdersByStatus(status);

        if (orders.isEmpty()) {
            throw new OrderNotFoundStatusException(status);
        }

        return orders;
    }
}
