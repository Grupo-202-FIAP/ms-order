package com.nexTime.order.application.usecases.implementation.order;

import com.nexTime.order.application.usecases.interfaces.order.ListOrdersUseCase;
import com.nexTime.order.domain.exception.order.OrderEmptyException;
import com.nexTime.order.domain.exception.order.OrderNotFoundException;
import com.nexTime.order.infrastructure.persistence.document.Order;
import com.nexTime.order.infrastructure.persistence.repository.IOrderRepository;
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
