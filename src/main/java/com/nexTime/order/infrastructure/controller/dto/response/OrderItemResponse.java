package com.nexTime.order.infrastructure.controller.dto.response;

import lombok.Builder;

import java.math.BigDecimal;
import java.util.UUID;

@Builder
public record OrderItemResponse(
    UUID id,
    ProductResponse product,
    int quantity,
    BigDecimal priceAtPurchase
) {
}
