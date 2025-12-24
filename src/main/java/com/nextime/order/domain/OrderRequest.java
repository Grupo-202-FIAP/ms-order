package com.nextime.order.domain;

import com.nextime.order.infrastructure.persistence.document.OrderItem;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequest {
    private List<OrderItem> orderItems;
}
