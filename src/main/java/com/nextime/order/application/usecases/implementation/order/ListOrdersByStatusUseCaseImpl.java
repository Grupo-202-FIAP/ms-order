package com.nextime.order.application.usecases.implementation.order;

import com.nextime.order.application.gateways.OrderRepositoryPort;
import com.nextime.order.application.usecases.interfaces.order.ListOrdersByStatusUseCase;
import com.nextime.order.domain.enums.OrderStatus;
import com.nextime.order.domain.exception.order.OrderNotFoundStatusException;
import com.nextime.order.infrastructure.persistence.document.Order;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ListOrdersByStatusUseCaseImpl implements ListOrdersByStatusUseCase {

    private final OrderRepositoryPort orderRepository;

    @Override
    public List<Order> execute(OrderStatus status) {

        final List<Order> orders = orderRepository.findByStatus(status);

        if (orders.isEmpty()) {
            throw new OrderNotFoundStatusException(status);
        }

        return orders;
    }
}
