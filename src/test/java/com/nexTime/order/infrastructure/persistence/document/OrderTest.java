package com.nexTime.order.infrastructure.persistence.document;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Testes para Order")
class OrderTest {

    private Order order;
    private List<OrderItem> items;

    @BeforeEach
    void setUp() {
        items = new ArrayList<>();
        Product product1 = Product.builder()
                .id(1L)
                .name("Hambúrguer")
                .unitPrice(new BigDecimal("25.50"))
                .build();

        Product product2 = Product.builder()
                .id(2L)
                .name("Batata Frita")
                .unitPrice(new BigDecimal("15.00"))
                .build();

        OrderItem item1 = OrderItem.builder()
                .id(UUID.randomUUID())
                .product(product1)
                .quantity(2)
                .build();

        OrderItem item2 = OrderItem.builder()
                .id(UUID.randomUUID())
                .product(product2)
                .quantity(3)
                .build();

        items.add(item1);
        items.add(item2);

        order = Order.builder()
                .id(UUID.randomUUID())
                .items(items)
                .build();
    }

    @Test
    @DisplayName("Deve calcular o preço total corretamente")
    void deveCalcularPrecoTotalCorretamente() {
        // When
        BigDecimal totalPrice = order.calculateTotalPrice();

        // Then
        // 25.50 * 2 + 15.00 * 3 = 51.00 + 45.00 = 96.00
        assertThat(totalPrice).isEqualByComparingTo(new BigDecimal("96.00"));
    }

    @Test
    @DisplayName("Deve retornar zero quando lista de itens estiver vazia")
    void deveRetornarZeroQuandoListaItensVazia() {
        // Given
        order.setItems(new ArrayList<>());

        // When
        BigDecimal totalPrice = order.calculateTotalPrice();

        // Then
        assertThat(totalPrice).isEqualByComparingTo(BigDecimal.ZERO);
    }

    @Test
    @DisplayName("Deve retornar zero quando lista de itens for null")
    void deveRetornarZeroQuandoListaItensNull() {
        // Given
        order.setItems(null);

        // When
        BigDecimal totalPrice = order.calculateTotalPrice();

        // Then
        assertThat(totalPrice).isEqualByComparingTo(BigDecimal.ZERO);
    }

    @Test
    @DisplayName("Deve gerar identificador único para o pedido")
    void deveGerarIdentificadorUnico() {
        // When
        String identifier1 = order.generateOrderId();
        String identifier2 = order.generateOrderId();

        // Then
        assertThat(identifier1).isNotEqualTo(identifier2);
        assertThat(identifier1).matches("ORD-[A-Z0-9]{4}-[0-9]{4}");
        assertThat(identifier2).matches("ORD-[A-Z0-9]{4}-[0-9]{4}");
    }

    @Test
    @DisplayName("Deve gerar identificador com formato correto")
    void deveGerarIdentificadorComFormatoCorreto() {
        // When
        String identifier = order.generateOrderId();

        // Then
        assertThat(identifier).startsWith("ORD-");
        assertThat(identifier).hasSize(13); // ORD-XXXX-XXXX = 4 + 4 + 1 + 4 = 13
        assertThat(identifier.split("-")).hasSize(3);
    }

    @Test
    @DisplayName("Deve calcular preço total com quantidade zero")
    void deveCalcularPrecoTotalComQuantidadeZero() {
        // Given
        Product product = Product.builder()
                .id(1L)
                .name("Produto")
                .unitPrice(new BigDecimal("10.00"))
                .build();

        OrderItem item = OrderItem.builder()
                .id(UUID.randomUUID())
                .product(product)
                .quantity(0)
                .build();

        order.setItems(List.of(item));

        // When
        BigDecimal totalPrice = order.calculateTotalPrice();

        // Then
        assertThat(totalPrice).isEqualByComparingTo(BigDecimal.ZERO);
    }

    @Test
    @DisplayName("Deve calcular preço total com múltiplos itens do mesmo produto")
    void deveCalcularPrecoTotalComMultiplosItensMesmoProduto() {
        // Given
        Product product = Product.builder()
                .id(1L)
                .name("Produto")
                .unitPrice(new BigDecimal("10.00"))
                .build();

        OrderItem item1 = OrderItem.builder()
                .id(UUID.randomUUID())
                .product(product)
                .quantity(5)
                .build();

        OrderItem item2 = OrderItem.builder()
                .id(UUID.randomUUID())
                .product(product)
                .quantity(3)
                .build();

        order.setItems(List.of(item1, item2));

        // When
        BigDecimal totalPrice = order.calculateTotalPrice();

        // Then
        // 10.00 * 5 + 10.00 * 3 = 50.00 + 30.00 = 80.00
        assertThat(totalPrice).isEqualByComparingTo(new BigDecimal("80.00"));
    }
}

