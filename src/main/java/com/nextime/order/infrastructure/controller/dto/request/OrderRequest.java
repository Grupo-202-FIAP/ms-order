package com.nextime.order.infrastructure.controller.dto.request;

import java.util.List;
import java.util.UUID;
import lombok.Builder;

@Builder
public record OrderRequest(
        List<OrderItemRequest> items,
        UUID customerId
) {
}
