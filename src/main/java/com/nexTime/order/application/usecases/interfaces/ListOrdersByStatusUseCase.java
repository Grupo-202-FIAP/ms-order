package com.nexTime.order.application.usecases.interfaces;


import com.nexTime.order.infrastructure.persistence.document.Order;

import java.util.List;

public interface ListOrdersByStatusUseCase {
    List<Order> execute( );
}
