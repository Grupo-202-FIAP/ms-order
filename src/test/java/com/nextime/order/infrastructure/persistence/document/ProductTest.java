package com.nextime.order.infrastructure.persistence.document;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

class ProductTest {

    @Test
    void shouldCreateProductWithBuilder() {
        // Given
        Long id = 1L;
        String name = "Product Name";
        BigDecimal unitPrice = BigDecimal.valueOf(10.50);

        // When
        Product product = Product.builder()
                .id(id)
                .name(name)
                .unitPrice(unitPrice)
                .build();

        // Then
        assertThat(product.getId()).isEqualTo(id);
        assertThat(product.getName()).isEqualTo(name);
        assertThat(product.getUnitPrice()).isEqualTo(unitPrice);
    }

    @Test
    void shouldCreateProductWithNoArgsConstructor() {
        // When
        Product product = new Product();

        // Then
        assertThat(product).isNotNull();
    }

    @Test
    void shouldCreateProductWithAllArgsConstructor() {
        // Given
        Long id = 1L;
        String name = "Product Name";
        BigDecimal unitPrice = BigDecimal.valueOf(10.50);

        // When
        Product product = new Product(id, name, unitPrice);

        // Then
        assertThat(product.getId()).isEqualTo(id);
        assertThat(product.getName()).isEqualTo(name);
        assertThat(product.getUnitPrice()).isEqualTo(unitPrice);
    }

    @Test
    void shouldSetAndGetProperties() {
        // Given
        Product product = new Product();
        Long id = 1L;
        String name = "Product Name";
        BigDecimal unitPrice = BigDecimal.valueOf(10.50);

        // When
        product.setId(id);
        product.setName(name);
        product.setUnitPrice(unitPrice);

        // Then
        assertThat(product.getId()).isEqualTo(id);
        assertThat(product.getName()).isEqualTo(name);
        assertThat(product.getUnitPrice()).isEqualTo(unitPrice);
    }
}

