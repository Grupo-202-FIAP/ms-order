package com.nextime.order.infrastructure.controller;

import com.nextime.order.application.gateways.LoggerRepositoryPort;
import com.nextime.order.application.usecases.interfaces.event.FindAllEventsUseCase;
import com.nextime.order.application.usecases.interfaces.event.FindEventByFiltersUseCase;
import com.nextime.order.domain.EventFilters;
import com.nextime.order.infrastructure.persistence.document.Event;
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
    private final LoggerRepositoryPort logger;

    @GetMapping("/filter")
    public ResponseEntity<Event> findByFilters(EventFilters filter) {
        logger.info("[EventController.findByFilters] Iniciando Busca de evento por filtros");
        final Event event = this.findEventByFiltersUseCase.execute(filter);
        logger.info("[EventController.findByFilters] Evento encontrado: id={}", event.getId());
        return ResponseEntity.ok(event);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Event>> findAll() {
        logger.info("[EventController.findAll] Iniciando Busca de todos os eventos");
        final List<Event> events = this.findAllEventsUseCase.execute();
        logger.info("[EventController.findAll] {} eventos encontrados", events.size());
        return ResponseEntity.ok(events);
    }
}
