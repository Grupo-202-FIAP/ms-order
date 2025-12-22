package com.nexTime.order.infrastructure.controller.dto.request;

import java.util.List;
import java.util.UUID;

public record OrderRequest(
        List<OrderItemRequest> itens
) {
}
