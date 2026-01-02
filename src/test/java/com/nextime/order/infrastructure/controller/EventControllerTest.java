package com.nextime.order.infrastructure.controller;

import com.nextime.order.application.gateways.LoggerRepositoryPort;
import com.nextime.order.application.usecases.interfaces.event.FindAllEventsUseCase;
import com.nextime.order.application.usecases.interfaces.event.FindEventByFiltersUseCase;
import com.nextime.order.domain.EventFilters;
import com.nextime.order.infrastructure.persistence.document.Event;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EventControllerTest {

    @Mock
    private FindEventByFiltersUseCase findEventByFiltersUseCase;

    @Mock
    private FindAllEventsUseCase findAllEventsUseCase;

    @Mock
    private LoggerRepositoryPort logger;

    @InjectMocks
    private EventController eventController;

    private Event event;
    private EventFilters filters;

    @BeforeEach
    void setUp() {
        event = Event.builder()
                .id(UUID.randomUUID())
                .orderId(UUID.randomUUID())
                .build();

        filters = new EventFilters(UUID.randomUUID(), null);
    }

    @Test
    void shouldFindEventByFiltersSuccessfully() {
        // Given
        when(findEventByFiltersUseCase.execute(filters)).thenReturn(event);

        // When
        ResponseEntity<Event> response = eventController.findByFilters(filters);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(event);
        verify(logger).info(any(String.class));
        verify(findEventByFiltersUseCase).execute(filters);
    }

    @Test
    void shouldFindAllEventsSuccessfully() {
        // Given
        Event event1 = Event.builder().id(UUID.randomUUID()).build();
        Event event2 = Event.builder().id(UUID.randomUUID()).build();
        List<Event> events = Arrays.asList(event1, event2);
        when(findAllEventsUseCase.execute()).thenReturn(events);

        // When
        ResponseEntity<List<Event>> response = eventController.findAll();

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).hasSize(2);
        assertThat(response.getBody()).containsExactly(event1, event2);
        verify(logger).info(any(String.class));
        verify(findAllEventsUseCase).execute();
    }
}



