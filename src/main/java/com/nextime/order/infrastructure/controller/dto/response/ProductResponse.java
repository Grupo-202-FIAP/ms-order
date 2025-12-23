package com.nextime.order.infrastructure.controller.dto.response;


import com.nextime.order.domain.enums.Category;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

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
