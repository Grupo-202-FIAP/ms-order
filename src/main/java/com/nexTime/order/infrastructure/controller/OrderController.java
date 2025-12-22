package com.nexTime.order.infrastructure.controller;

import com.nexTime.order.application.gateways.LoggerPort;
import com.nexTime.order.application.mapper.OrderMapper;
import com.nexTime.order.application.usecases.interfaces.CreateOrderUseCase;
import com.nexTime.order.application.usecases.interfaces.ListOrdersByStatusUseCase;
import com.nexTime.order.application.usecases.interfaces.ListOrdersUseCase;
import com.nexTime.order.application.usecases.interfaces.UpdateOrderStatusUseCase;
import com.nexTime.order.domain.OrderRequest;
import com.nexTime.order.infrastructure.adapters.OrderAdapter;
import com.nexTime.order.infrastructure.controller.dto.response.OrderResponse;
import com.nexTime.order.infrastructure.persistence.document.Order;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@AllArgsConstructor
@RequestMapping("/api/order")
public class OrderController {

//    private final CreateOrderUseCase createOrderUseCase;
//    private final ListOrdersUseCase listOrdersUseCase;
//    private final ListOrdersByStatusUseCase listOrdersByStatusUseCase;
//    private final UpdateOrderStatusUseCase updateOrderUseCase;
//    private final OrderAdapter orderAdapter;
//    private final LoggerPort logger;

//    @PostMapping("/create")
//    public ResponseEntity<OrderResponse> createOrder(@Valid @RequestBody OrderRequest orderRequest) {
//        logger.info("[Order] Iniciando criação de pedido");
//        Order domain = OrderMapper.toDomain(orderRequest);
//        domain = this.createOrderUseCase.execute(domain);
//        final OrderResponse response = OrderMapper.toResponse(domain);
//        logger.info("[Order] Pedido criado com sucesso: id={}", domain.getId());
//        return ResponseEntity.ok(response);
//    }

//    @GetMapping()
//    public ResponseEntity<List<OrderResponse>> listOrders() {
//        logger.info("[Order] Buscando todos os pedidos");
//        final List<Order> orders = this.listOrdersUseCase.execute();
//        logger.info("[Order] {} pedidos encontrados", orders.size());
//        return ResponseEntity.ok(orders);
//    }

//    @GetMapping("/status")
//    public ResponseEntity<List<OrderResponse>> listOrderByStatus() {
//        logger.info("[Order] Buscando pedidos ordenados por status");
//        final List<Order> orders = this.listOrdersByStatusUseCase.execute();
//        final List<OrderResponse> response = orders.stream()
//                .map(OrderMapper::toResponse)
//                .toList();
//        logger.info("[Order] {} pedidos encontrados ", orders.size());
//        return ResponseEntity.ok(response);
//    }
//
//    @PutMapping("/status")
//    public ResponseEntity<OrderResponse> updateStatus(@RequestParam UUID orderID) {
//        logger.info("[Order] Atualizando status do pedido com id={}", orderID);
//        final Order order = updateOrderUseCase.execute(orderID);
//        final OrderResponse response = OrderMapper.toResponse(order);
//        logger.info("[Order] Status do pedido atualizado: id={}, novoStatus={}", order.getId(), order.getStatus());
//        return ResponseEntity.ok().body(response);
//    }




}
