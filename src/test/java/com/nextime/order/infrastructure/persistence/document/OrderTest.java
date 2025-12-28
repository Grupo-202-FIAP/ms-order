package com.nextime.order.infrastructure.persistence.document;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;

class OrderTest {

    private Order order;

    @BeforeEach
    void setUp() {
        order = Order.builder().build();
    }

    @Test
    void shouldCalculateTotalPriceAsZeroWhenItemsIsNull() {
        // When
        BigDecimal result = order.calculateTotalPrice();

        // Then
        assertThat(result).isEqualTo(BigDecimal.ZERO);
    }

    @Test
    void shouldCalculateTotalPriceAsZeroWhenItemsIsEmpty() {
        // Given
        order.setItems(new ArrayList<>());

        // When
        BigDecimal result = order.calculateTotalPrice();

        // Then
        assertThat(result).isEqualTo(BigDecimal.ZERO);
    }

    @Test
    void shouldCalculateTotalPriceSuccessfully() {
        // Given
        Product product1 = Product.builder()
                .id(1L)
                .name("Product 1")
                .unitPrice(BigDecimal.valueOf(10.0))
                .build();
        Product product2 = Product.builder()
                .id(2L)
                .name("Product 2")
                .unitPrice(BigDecimal.valueOf(20.0))
                .build();

        OrderItem item1 = OrderItem.builder()
                .product(product1)
                .quantity(2)
                .build();
        OrderItem item2 = OrderItem.builder()
                .product(product2)
                .quantity(3)
                .build();

        List<OrderItem> items = List.of(item1, item2);
        order.setItems(items);

        // When
        BigDecimal result = order.calculateTotalPrice();

        // Then
        // (10.0 * 2) + (20.0 * 3) = 20.0 + 60.0 = 80.0
        assertThat(result).isEqualByComparingTo(BigDecimal.valueOf(80.0));
    }

    @Test
    void shouldGenerateOrderIdWithCorrectFormat() {
        // When
        String orderId = order.generateOrderId();

        // Then
        assertThat(orderId).isNotNull();
        assertThat(orderId).startsWith("ORD-");
        assertThat(orderId.length()).isGreaterThan(8);
    }

    @Test
    void shouldGenerateDifferentOrderIds() {
        // When
        String orderId1 = order.generateOrderId();
        String orderId2 = order.generateOrderId();

        // Then
        assertThat(orderId1).isNotEqualTo(orderId2);
    }
}


