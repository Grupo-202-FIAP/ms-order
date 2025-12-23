package com.nextime.order.infrastructure.controller.dto.request;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record OrderItemRequest(
        Long productId,
        Integer quantity,
        BigDecimal priceAtPurchase
) {
}
