package com.nexTime.order.infrastructure.controller;

import com.nexTime.order.application.gateways.LoggerPort;
import com.nexTime.order.application.mapper.OrderMapper;
import com.nexTime.order.application.usecases.interfaces.order.CreateOrderUseCase;
import com.nexTime.order.application.usecases.interfaces.order.ListOrdersByStatusUseCase;
import com.nexTime.order.application.usecases.interfaces.order.ListOrdersUseCase;
import com.nexTime.order.domain.enums.OrderStatus;
import com.nexTime.order.infrastructure.controller.dto.request.OrderRequest;
import com.nexTime.order.infrastructure.controller.dto.response.OrderResponse;
import com.nexTime.order.infrastructure.persistence.document.Order;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/order")
public class OrderController {

    private final CreateOrderUseCase createOrderUseCase;
    private final ListOrdersUseCase listOrdersUseCase;
    private final ListOrdersByStatusUseCase listOrdersByStatusUseCase;
    private final OrderMapper orderMapper;
    private final LoggerPort logger;

    @PostMapping("/create")
    public ResponseEntity<OrderResponse> createOrder(@Valid @RequestBody OrderRequest orderRequest) {
        logger.info("[Order] Iniciando criação de pedido");
        Order domain = orderMapper.toDomain(orderRequest);
        domain = this.createOrderUseCase.execute(domain);
        final OrderResponse response = orderMapper.toResponse(domain);
        logger.info("[Order] Pedido criado com sucesso: id={}", domain.getId());
        return ResponseEntity.ok(response);
    }

    @GetMapping()
    public ResponseEntity<List<OrderResponse>> listOrders() {
        logger.info("[Order] Buscando todos os pedidos");
        final List<Order> orders = this.listOrdersUseCase.execute();
        final List<OrderResponse> response = orders.stream()
                .map(orderMapper::toResponse)
                .toList();

        logger.info("[Order] {} pedidos encontrados", orders.size());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/status")
    public ResponseEntity<List<OrderResponse>> listOrderByStatus(@RequestParam OrderStatus status) {
        logger.info("[Order] Buscando pedidos ordenados por status");
        final List<Order> orders = this.listOrdersByStatusUseCase.execute(status);
        final List<OrderResponse> response = orders.stream()
                .map(orderMapper::toResponse)
                .toList();
        logger.info("[Order] {} pedidos encontrados ", orders.size());
        return ResponseEntity.ok(response);
    }

}