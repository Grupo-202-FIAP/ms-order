package com.nextime.order.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventFilters {
    private UUID orderId;
    private UUID transactionId;

    public boolean hasOrderId() {
        return orderId != null;
    }

    public boolean hasTransactionId() {
        return transactionId != null;
    }

    public boolean isEmpty() {
        return orderId == null && transactionId == null;
    }

}
