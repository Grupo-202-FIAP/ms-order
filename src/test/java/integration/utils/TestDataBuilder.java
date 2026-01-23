package integration.utils;

import com.nextime.order.domain.enums.OrderStatus;
import com.nextime.order.domain.enums.PaymentStatus;
import com.nextime.order.infrastructure.controller.dto.request.OrderItemRequest;
import com.nextime.order.infrastructure.controller.dto.request.OrderRequest;
import com.nextime.order.infrastructure.persistence.document.Event;
import com.nextime.order.infrastructure.persistence.document.Order;
import com.nextime.order.infrastructure.persistence.document.OrderItem;
import com.nextime.order.infrastructure.persistence.document.Product;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TestDataBuilder {

    public static OrderRequest buildValidOrderRequest(UUID customerId, List<OrderItemRequest> items) {
        return OrderRequest.builder()
                .customerId(customerId)
                .items(items)
                .build();
    }

    public static OrderRequest buildValidOrderRequest(UUID customerId) {
        List<OrderItemRequest> items = List.of(
                OrderItemRequest.builder()
                        .productId(1L)
                        .quantity(2)
                        .priceAtPurchase(new BigDecimal("50.00"))
                        .build()
        );
        return buildValidOrderRequest(customerId, items);
    }

    public static OrderItemRequest buildOrderItemRequest(Long productId, Integer quantity, BigDecimal price) {
        return OrderItemRequest.builder()
                .productId(productId)
                .quantity(quantity)
                .priceAtPurchase(price)
                .build();
    }

    public static Order buildOrder(UUID customerId, OrderStatus status) {
        Product product = Product.builder()
                .id(1L)
                .name("Test Product")
                .unitPrice(new BigDecimal("50.00"))
                .build();

        List<OrderItem> items = new ArrayList<>();
        items.add(OrderItem.builder()
                .product(product)
                .quantity(2)
                .build());

        return Order.builder()
                .id(UUID.randomUUID())
                .customerId(customerId)
                .status(status)
                .paymentStatus(PaymentStatus.PENDING)
                .items(items)
                .createdAt(LocalDateTime.now())
                .build();
    }

    public static Event buildEvent(UUID orderId, UUID transactionId, String status) {
        Product product = Product.builder()
                .id(1L)
                .name("Test Product")
                .unitPrice(new BigDecimal("50.00"))
                .build();

        Order order = Order.builder()
                .id(orderId)
                .customerId(UUID.randomUUID())
                .status(OrderStatus.RECEIVED)
                .paymentStatus(PaymentStatus.PENDING)
                .items(List.of(
                        OrderItem.builder()
                                .product(product)
                                .quantity(1)
                                .build()
                ))
                .createdAt(LocalDateTime.now())
                .build();

        return Event.builder()
                .id(UUID.randomUUID())
                .orderId(orderId)
                .transactionId(transactionId)
                .payload(order)
                .source("payment-service")
                .status(status)
                .history(new ArrayList<>())
                .createdAt(LocalDateTime.now())
                .build();
    }

    public static Event buildEvent(UUID orderId) {
        return buildEvent(orderId, UUID.randomUUID(), "PROCESSED");
    }

    public static String buildValidEventJson(UUID orderId, UUID transactionId, String status) {
        return String.format("""
                {
                  "id": "%s",
                  "transactionId": "%s",
                  "orderId": "%s",
                  "source": "payment-service",
                  "status": "%s",
                  "payload": {
                    "id": "%s",
                    "customerId": "%s",
                    "status": "RECEIVED",
                    "paymentStatus": "PROCESSED",
                    "items": [
                      {
                        "productId": 1,
                        "quantity": 2,
                        "priceAtPurchase": 50.00
                      }
                    ]
                  }
                }
                """,
                UUID.randomUUID(),
                transactionId,
                orderId,
                status,
                orderId,
                UUID.randomUUID()
        );
    }

    public static String buildInvalidEventJsonMissingOrderId() {
        return """
                {
                  "id": "223e4567-e89b-12d3-a456-426614174000",
                  "transactionId": "323e4567-e89b-12d3-a456-426614174000",
                  "source": "payment-service",
                  "status": "PROCESSED"
                }
                """;
    }

    public static String buildMalformedJson() {
        return "{ \"invalid json without closing brace\"";
    }
}

