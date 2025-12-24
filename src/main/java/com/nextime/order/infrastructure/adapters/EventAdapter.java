package com.nextime.order.infrastructure.adapters;

import com.nextime.order.application.gateways.EventPort;
import com.nextime.order.application.gateways.LoggerPort;
import com.nextime.order.infrastructure.persistence.document.Event;
import com.nextime.order.infrastructure.persistence.repository.IEventRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;

@Repository
@AllArgsConstructor
public class EventAdapter implements EventPort {

    private final IEventRepository eventRepository;
    private final LoggerPort logger;

    public void notifyEvent(Event event) {
        event.setId(event.getId());
        event.setCreatedAt(LocalDateTime.now());
        save(event);
        logger.info("[EventAdapter.notifyEvent] Pedido {} notificado com o id {}", event.getOrderId(), event.getTransactionId());
    }

    public Event save(Event event) {
        try {
            logger.info("[EventAdapter.save] Salvando evento: {}", event);
            return eventRepository.save(event);
        } catch (Exception e) {
            logger.error("[EventAdapter.save] Erro ao salvar evento: {}", e);
            throw new RuntimeException("[EventAdapter.save] Erro ao salvar evento: {}", e);
        }
    }

}
