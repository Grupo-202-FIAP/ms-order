package com.nextime.order.application.usecases.implementation.event;

import com.nextime.order.application.gateways.LoggerPort;
import com.nextime.order.application.usecases.interfaces.event.NotifyEventUseCase;
import com.nextime.order.application.usecases.interfaces.event.SaveEventUseCase;
import com.nextime.order.infrastructure.persistence.document.Event;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class NotifyEventUseCaseImpl implements NotifyEventUseCase {

    private final SaveEventUseCase saveEventUseCase;
    private final LoggerPort logger;


    @Override
    public void execute(Event event) {
        event.setId(event.getId());
        event.setCreatedAt(LocalDateTime.now());
        saveEventUseCase.execute(event);
        logger.info("[EventAdapter.notifyEvent] Pedido {} notificado com o id {}", event.getOrderId(), event.getTransactionId());
    }

}
