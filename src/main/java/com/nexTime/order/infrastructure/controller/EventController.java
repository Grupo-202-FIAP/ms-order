package com.nexTime.order.infrastructure.controller;

import com.nexTime.order.application.gateways.LoggerPort;
import com.nexTime.order.application.usecases.interfaces.event.FindAllEventsUseCase;
import com.nexTime.order.application.usecases.interfaces.event.FindEventByFiltersUseCase;
import com.nexTime.order.application.usecases.interfaces.order.CreateOrderUseCase;
import com.nexTime.order.application.usecases.interfaces.order.ListOrdersUseCase;
import com.nexTime.order.domain.EventFilters;
import com.nexTime.order.infrastructure.persistence.document.Event;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/event")
@AllArgsConstructor
public class EventController {

    private final FindEventByFiltersUseCase findEventByFiltersUseCase;
    private final FindAllEventsUseCase findAllEventsUseCase;
    private final LoggerPort logger;

    @GetMapping("/filter")
    public ResponseEntity<Event> findByFilters(EventFilters filter) {
        logger.info("[Event] Iniciando Busca de evento por filtros");
        final Event event = this.findEventByFiltersUseCase.execute(filter);
        logger.info("[Event] Evento encontrado: id={}", event.getId());
        return ResponseEntity.ok(event);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Event>> findAll() {
        logger.info("[Event] Iniciando Busca de todos os eventos");
        final List<Event> events = this.findAllEventsUseCase.execute();
        logger.info("[Event] {} eventos encontrados", events.size());
        return ResponseEntity.ok(events);
    }
}
