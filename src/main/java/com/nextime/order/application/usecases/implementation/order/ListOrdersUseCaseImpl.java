package com.nextime.order.application.usecases.implementation.order;

import com.nextime.order.application.usecases.interfaces.order.ListOrdersUseCase;
import com.nextime.order.domain.exception.order.OrderEmptyException;
import com.nextime.order.infrastructure.persistence.document.Order;
import com.nextime.order.infrastructure.persistence.repository.IOrderRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ListOrdersUseCaseImpl implements ListOrdersUseCase {

    private final IOrderRepository orderRepositoryPort;

    @Override
    public List<Order> execute() {
        List<Order> orders = this.orderRepositoryPort.findAll();

        if (orders.isEmpty()) {
            throw new OrderEmptyException();
        }

        return orders;

    }
}
