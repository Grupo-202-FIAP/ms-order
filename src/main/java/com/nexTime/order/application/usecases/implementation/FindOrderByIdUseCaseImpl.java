package com.nexTime.order.application.usecases.implementation;



import com.nexTime.order.application.usecases.interfaces.FindOrderByIdUseCase;
import com.nexTime.order.domain.exception.OrderNotFoundException;
import com.nexTime.order.infrastructure.persistence.document.Order;
import com.nexTime.order.infrastructure.persistence.repository.IOrderRepository;

import java.util.UUID;

public class FindOrderByIdUseCaseImpl implements FindOrderByIdUseCase {

    private final IOrderRepository orderRepositoryPort;

    public FindOrderByIdUseCaseImpl(IOrderRepository orderRepositoryPort) {
        this.orderRepositoryPort = orderRepositoryPort;
    }

    @Override
    public Order execute(UUID orderId) {
        return orderRepositoryPort.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(orderId));
    }
}
