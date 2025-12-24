package com.nextime.order.application.usecases.implementation.event;

import com.nextime.order.application.gateways.LoggerPort;
import com.nextime.order.application.usecases.interfaces.event.SaveEventUseCase;
import com.nextime.order.infrastructure.persistence.document.Event;
import com.nextime.order.infrastructure.persistence.repository.IEventRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class SaveEventUseCaseImpl implements SaveEventUseCase {

    private final IEventRepository eventRepository;
    private final LoggerPort logger;

    @Override
    public Event execute(Event event) {
        try {
            logger.info("[EventAdapter.save] Salvando evento: {}", event);
            return eventRepository.save(event);
        } catch (Exception e) {
            logger.error("[EventAdapter.save] Erro ao salvar evento: {}", e);
            throw new RuntimeException("[EventAdapter.save] Erro ao salvar evento: {}", e);
        }
    }
}
