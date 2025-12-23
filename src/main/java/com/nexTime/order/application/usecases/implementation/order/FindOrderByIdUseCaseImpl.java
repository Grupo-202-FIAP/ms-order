package com.nexTime.order.application.usecases.implementation.order;



import com.nexTime.order.application.usecases.interfaces.order.FindOrderByIdUseCase;
import com.nexTime.order.domain.exception.order.OrderNotFoundException;
import com.nexTime.order.infrastructure.persistence.document.Order;
import com.nexTime.order.infrastructure.persistence.repository.IOrderRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
public class FindOrderByIdUseCaseImpl implements FindOrderByIdUseCase {

    private final IOrderRepository orderRepositoryPort;

    @Override
    public Order execute(UUID orderId) {
        return orderRepositoryPort.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(orderId));
    }
}
