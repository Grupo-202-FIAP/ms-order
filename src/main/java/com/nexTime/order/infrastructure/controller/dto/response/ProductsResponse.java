package com.nexTime.order.infrastructure.controller.dto.response;


import com.nexTime.order.domain.enums.Category;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Builder
public record ProductsResponse(
        Long id,
        String name,
        @Enumerated(EnumType.STRING)
        Category category,
        BigDecimal unitPrice,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
