package com.nextime.order.domain;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventFilters {
    private UUID orderId;
    private UUID transactionId;
}
