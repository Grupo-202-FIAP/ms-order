package com.nextime.order.infrastructure.adapters;

import com.nextime.order.application.gateways.LoggerRepositoryPort;
import com.nextime.order.application.gateways.OrderRepositoryPort;
import com.nextime.order.domain.enums.OrderStatus;
import com.nextime.order.infrastructure.exception.RepositoryException;
import com.nextime.order.infrastructure.persistence.document.Order;
import com.nextime.order.infrastructure.persistence.repository.IOrderRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class OrderRepositoryAdapter implements OrderRepositoryPort {

    private final IOrderRepository orderRepository;
    private final LoggerRepositoryPort logger;

    @Override
    public Order save(Order order) {
        logger.info("[OrderRepositoryAdapter.save] Salvando pedido id={}", order.getId());
        try {
            return orderRepository.save(order);
        } catch (DataAccessException ex) {
            logger.error("[OrderRepositoryAdapter.save] Erro ao salvar pedido id={}: {}", order.getId(), ex.getMessage());
            throw new RepositoryException("Erro ao salvar pedido", ex);
        }
    }

    @Override
    public Optional<Order> findById(UUID orderId) {
        logger.info("[OrderRepositoryAdapter.findById] Buscando pedido id={}", orderId);
        return orderRepository.findById(orderId);
    }

    @Override
    public List<Order> findByStatus(OrderStatus status) {
        logger.info("[OrderRepositoryAdapter.findByStatus] Buscando pedidos com status={}", status);
        return orderRepository.findOrdersByStatus(status);
    }

    @Override
    public List<Order> findAll() {
        logger.info("[OrderRepositoryAdapter.findAll] Buscando todos os pedidos");
        return orderRepository.findAll();
    }
}
