package com.nextime.order.infrastructure.controller.dto.response;

import java.math.BigDecimal;
import java.util.UUID;
import lombok.Builder;

@Builder
public record OrderItemResponse(
        UUID id,
        ProductResponse product,
        int quantity,
        BigDecimal priceAtPurchase
) {
}
