package com.nextime.order.application.usecases.implementation.order;

import com.nextime.order.application.gateways.OrderRepositoryPort;
import com.nextime.order.application.usecases.interfaces.order.FindOrderByIdUseCase;
import com.nextime.order.domain.exception.order.OrderNotFoundException;
import com.nextime.order.infrastructure.persistence.document.Order;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class FindOrderByIdUseCaseImpl implements FindOrderByIdUseCase {

    private final OrderRepositoryPort orderRepository;

    @Override
    public Order execute(UUID orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(orderId));
    }
}
