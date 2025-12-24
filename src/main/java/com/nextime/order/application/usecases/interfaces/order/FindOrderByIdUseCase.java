package com.nextime.order.application.usecases.interfaces.order;

import com.nextime.order.infrastructure.persistence.document.Order;
import java.util.UUID;

public interface FindOrderByIdUseCase {
    Order execute(UUID orderId);
}
