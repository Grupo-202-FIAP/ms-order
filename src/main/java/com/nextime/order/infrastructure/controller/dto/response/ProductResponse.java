package com.nextime.order.infrastructure.controller.dto.response;


import com.nextime.order.domain.enums.Category;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Builder;

@Builder
public record ProductResponse(
        Long id,
        String name,
        Category category,
        BigDecimal unitPrice,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
