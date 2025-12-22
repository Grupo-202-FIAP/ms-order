package com.nexTime.order.application.usecases.interfaces;


import com.nexTime.order.infrastructure.persistence.document.Order;

import java.util.UUID;

public interface UpdateOrderStatusUseCase {
    Order execute(UUID orderID);
}
