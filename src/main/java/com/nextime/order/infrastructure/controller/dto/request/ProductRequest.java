package com.nextime.order.infrastructure.controller.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record ProductRequest(
        @NotBlank(message = "Product name is required")
        @Size(max = 100, message = "Product name must not exceed 100 characters")
        String name,

        @NotNull(message = "Unit price is required")
        @DecimalMin(value = "0.0", inclusive = false, message = "Unit price must be greater than zero")
        BigDecimal unitPrice
) {
}
