package com.nexTime.order.infrastructure.controller.dto.request;

import lombok.Builder;

import java.util.List;
import java.util.UUID;

@Builder
public record OrderRequest(
        List<OrderItemRequest> items,
        UUID customerId
) {
}
