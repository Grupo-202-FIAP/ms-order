package com.nextime.order.application.usecases.interfaces.order;


import com.nextime.order.infrastructure.persistence.document.Order;

import java.util.List;

public interface ListOrdersUseCase {
    List<Order> execute();
}
