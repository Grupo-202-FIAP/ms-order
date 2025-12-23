package com.nexTime.order.application.usecases.implementation.order;


import com.nexTime.order.application.usecases.interfaces.order.CreateOrderUseCase;
import com.nexTime.order.domain.enums.OrderStatus;
import com.nexTime.order.domain.exception.order.OrderListEmptyException;
import com.nexTime.order.infrastructure.persistence.document.Order;
import com.nexTime.order.infrastructure.persistence.repository.IOrderRepository;
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
