package com.nextime.order.infrastructure.controller;

import com.nextime.order.application.gateways.LoggerPort;
import com.nextime.order.application.mapper.OrderMapper;
import com.nextime.order.application.usecases.interfaces.order.CreateOrderUseCase;
import com.nextime.order.application.usecases.interfaces.order.ListOrdersByStatusUseCase;
import com.nextime.order.application.usecases.interfaces.order.ListOrdersUseCase;
import com.nextime.order.domain.enums.OrderStatus;
import com.nextime.order.infrastructure.controller.dto.request.OrderRequest;
import com.nextime.order.infrastructure.controller.dto.response.OrderResponse;
import com.nextime.order.infrastructure.persistence.document.Order;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
