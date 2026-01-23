package com.nextime.order.infrastructure.controller.dto.request;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class ProductRequestTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void shouldCreateValidProductRequest() {
        // Given
        String name = "Product Name";
        BigDecimal unitPrice = BigDecimal.valueOf(10.50);

        // When
        ProductRequest request = new ProductRequest(name, unitPrice);

        // Then
        assertThat(request.name()).isEqualTo(name);
        assertThat(request.unitPrice()).isEqualTo(unitPrice);
    }

    @Test
    void shouldCreateProductRequestWithBuilder() {
        // Given
        String name = "Product Name";
        BigDecimal unitPrice = BigDecimal.valueOf(10.50);

        // When
        ProductRequest request = ProductRequest.builder()
                .name(name)
                .unitPrice(unitPrice)
                .build();

        // Then
        assertThat(request.name()).isEqualTo(name);
        assertThat(request.unitPrice()).isEqualTo(unitPrice);
    }

    @Test
    void shouldValidateNameNotBlank() {
        // Given
        ProductRequest request = new ProductRequest("", BigDecimal.valueOf(10.50));

        // When
        Set<ConstraintViolation<ProductRequest>> violations = validator.validate(request);

        // Then
        assertThat(violations).isNotEmpty();
        assertThat(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("name")))
                .isTrue();
    }

    @Test
    void shouldValidateNameSize() {
        // Given
        String longName = "a".repeat(101);
        ProductRequest request = new ProductRequest(longName, BigDecimal.valueOf(10.50));

        // When
        Set<ConstraintViolation<ProductRequest>> violations = validator.validate(request);

        // Then
        assertThat(violations).isNotEmpty();
        assertThat(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("name")))
                .isTrue();
    }

    @Test
    void shouldValidateUnitPriceNotNull() {
        // Given
        ProductRequest request = new ProductRequest("Product Name", null);

        // When
        Set<ConstraintViolation<ProductRequest>> violations = validator.validate(request);

        // Then
        assertThat(violations).isNotEmpty();
        assertThat(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("unitPrice")))
                .isTrue();
    }

    @Test
    void shouldValidateUnitPriceGreaterThanZero() {
        // Given
        ProductRequest request = new ProductRequest("Product Name", BigDecimal.ZERO);

        // When
        Set<ConstraintViolation<ProductRequest>> violations = validator.validate(request);

        // Then
        assertThat(violations).isNotEmpty();
        assertThat(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("unitPrice")))
                .isTrue();
    }
}

