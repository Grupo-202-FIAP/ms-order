package com.nextime.order.application.usecases.implementation.order;

import com.nextime.order.application.gateways.OrderRepositoryPort;
import com.nextime.order.application.usecases.interfaces.order.ListOrdersUseCase;
import com.nextime.order.domain.exception.order.OrderEmptyException;
import com.nextime.order.infrastructure.persistence.document.Order;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ListOrdersUseCaseImpl implements ListOrdersUseCase {

    private final OrderRepositoryPort orderRepository;

    @Override
    public List<Order> execute() {

        final List<Order> orders = orderRepository.findAll();

        if (orders.isEmpty()) {
            throw new OrderEmptyException();
        }

        return orders;
    }
}
