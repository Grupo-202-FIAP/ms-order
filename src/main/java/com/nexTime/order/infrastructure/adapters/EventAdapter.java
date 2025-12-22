package com.nexTime.order.infrastructure.adapters;

import com.nexTime.order.application.gateways.EventPort;
import com.nexTime.order.application.gateways.LoggerPort;
import com.nexTime.order.infrastructure.persistence.document.Event;
import com.nexTime.order.infrastructure.persistence.repository.IEventRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
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
