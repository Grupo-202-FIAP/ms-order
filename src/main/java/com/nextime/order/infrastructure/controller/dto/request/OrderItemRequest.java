package com.nextime.order.infrastructure.controller.dto.request;

import java.math.BigDecimal;
import lombok.Builder;

@Builder
public record OrderItemRequest(
        Long productId,
        Integer quantity,
        BigDecimal priceAtPurchase
) {
}
