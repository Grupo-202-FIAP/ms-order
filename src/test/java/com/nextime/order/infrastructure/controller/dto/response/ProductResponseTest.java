package com.nextime.order.infrastructure.controller.dto.response;

import com.nextime.order.domain.enums.Category;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class ProductResponseTest {

    @Test
    void shouldCreateProductResponse() {
        // Given
        Long id = 1L;
        String name = "Product Name";
        Category category = Category.SANDWICHES;
        BigDecimal unitPrice = BigDecimal.valueOf(10.50);
        LocalDateTime createdAt = LocalDateTime.now();
        LocalDateTime updatedAt = LocalDateTime.now();

        // When
        ProductResponse response = new ProductResponse(id, name, category, unitPrice, createdAt, updatedAt);

        // Then
        assertThat(response.id()).isEqualTo(id);
        assertThat(response.name()).isEqualTo(name);
        assertThat(response.category()).isEqualTo(category);
        assertThat(response.unitPrice()).isEqualTo(unitPrice);
        assertThat(response.createdAt()).isEqualTo(createdAt);
        assertThat(response.updatedAt()).isEqualTo(updatedAt);
    }

    @Test
    void shouldCreateProductResponseWithBuilder() {
        // Given
        Long id = 1L;
        String name = "Product Name";
        Category category = Category.SANDWICHES;
        BigDecimal unitPrice = BigDecimal.valueOf(10.50);
        LocalDateTime createdAt = LocalDateTime.now();
        LocalDateTime updatedAt = LocalDateTime.now();

        // When
        ProductResponse response = ProductResponse.builder()
                .id(id)
                .name(name)
                .category(category)
                .unitPrice(unitPrice)
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .build();

        // Then
        assertThat(response.id()).isEqualTo(id);
        assertThat(response.name()).isEqualTo(name);
        assertThat(response.category()).isEqualTo(category);
        assertThat(response.unitPrice()).isEqualTo(unitPrice);
        assertThat(response.createdAt()).isEqualTo(createdAt);
        assertThat(response.updatedAt()).isEqualTo(updatedAt);
    }
}

