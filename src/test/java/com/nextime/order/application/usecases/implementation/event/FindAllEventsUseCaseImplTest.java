package com.nextime.order.application.usecases.implementation.event;

import com.nextime.order.application.gateways.EventRepositoryPort;
import com.nextime.order.domain.exception.event.EventNotFoundException;
import com.nextime.order.infrastructure.persistence.document.Event;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FindAllEventsUseCaseImplTest {

    @Mock
    private EventRepositoryPort eventRepository;

    @InjectMocks
    private FindAllEventsUseCaseImpl findAllEventsUseCase;

    @Test
    void shouldFindAllEventsSuccessfully() {
        // Given
        Event event1 = Event.builder().id(UUID.randomUUID()).build();
        Event event2 = Event.builder().id(UUID.randomUUID()).build();
        List<Event> events = Arrays.asList(event1, event2);
        when(eventRepository.findAllOrderedByCreationDate()).thenReturn(events);

        // When
        List<Event> result = findAllEventsUseCase.execute();

        // Then
        assertThat(result).isNotNull();
        assertThat(result).hasSize(2);
        assertThat(result).containsExactly(event1, event2);
    }

    @Test
    void shouldThrowEventNotFoundExceptionWhenNoEventsFound() {
        // Given
        when(eventRepository.findAllOrderedByCreationDate()).thenReturn(new ArrayList<>());

        // When/Then
        assertThatThrownBy(() -> findAllEventsUseCase.execute())
                .isInstanceOf(EventNotFoundException.class);
    }
}

