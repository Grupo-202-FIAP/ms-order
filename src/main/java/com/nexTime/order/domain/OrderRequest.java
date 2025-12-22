package com.nexTime.order.domain;

import com.nexTime.order.infrastructure.persistence.document.OrderItem;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequest {
    private List<OrderItem> orderItems;
}
