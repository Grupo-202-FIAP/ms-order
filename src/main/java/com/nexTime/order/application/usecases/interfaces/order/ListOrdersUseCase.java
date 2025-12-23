package com.nexTime.order.application.usecases.interfaces.order;


import com.nexTime.order.infrastructure.persistence.document.Order;

import java.util.List;

public interface ListOrdersUseCase {
    List<Order> execute();
}
