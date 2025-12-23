package com.nexTime.order.application.usecases.implementation.event;

import com.nexTime.order.domain.exception.event.EventNotFoundException;
import com.nexTime.order.infrastructure.persistence.document.Event;
import com.nexTime.order.infrastructure.persistence.document.Order;
import com.nexTime.order.infrastructure.persistence.repository.IEventRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes para FindAllEventsUseCaseImpl")
class FindAllEventsUseCaseImplTest {

    @Mock
    private IEventRepository eventRepository;

    @InjectMocks
    private FindAllEventsUseCaseImpl findAllEventsUseCase;

    private List<Event> events;

    @BeforeEach
    void setUp() {
        events = new ArrayList<>();
    }

    @Test
    @DisplayName("Deve listar todos os eventos com sucesso")
    void deveListarTodosEventosComSucesso() {
        // Given
        UUID orderId1 = UUID.randomUUID();
        UUID orderId2 = UUID.randomUUID();

        Event event1 = Event.builder()
                .id(UUID.randomUUID())
                .orderId(orderId1)
                .transactionId(UUID.randomUUID())
                .payload(Order.builder().id(orderId1).build())
                .createdAt(LocalDateTime.now())
                .build();

        Event event2 = Event.builder()
                .id(UUID.randomUUID())
                .orderId(orderId2)
                .transactionId(UUID.randomUUID())
                .payload(Order.builder().id(orderId2).build())
                .createdAt(LocalDateTime.now())
                .build();

        events = Arrays.asList(event1, event2);
        when(eventRepository.findAllByOrderByCreatedAtDesc()).thenReturn(events);

        // When
        List<Event> result = findAllEventsUseCase.execute();

        // Then
        assertThat(result).isNotNull();
        assertThat(result).hasSize(2);
        assertThat(result).containsExactlyInAnyOrder(event1, event2);
        verify(eventRepository, times(1)).findAllByOrderByCreatedAtDesc();
    }

    @Test
    @DisplayName("Deve lançar exceção quando não houver eventos")
    void deveLancarExcecaoQuandoNaoHouverEventos() {
        // Given
        when(eventRepository.findAllByOrderByCreatedAtDesc()).thenReturn(new ArrayList<>());

        // When & Then
        assertThatThrownBy(() -> findAllEventsUseCase.execute())
                .isInstanceOf(EventNotFoundException.class)
                .hasMessageContaining("Evento não encontrado");

        verify(eventRepository, times(1)).findAllByOrderByCreatedAtDesc();
    }

    @Test
    @DisplayName("Deve retornar lista ordenada por data de criação descendente")
    void deveRetornarListaOrdenadaPorDataCriacao() {
        // Given
        LocalDateTime now = LocalDateTime.now();
        Event event1 = Event.builder()
                .id(UUID.randomUUID())
                .createdAt(now.minusHours(2))
                .build();

        Event event2 = Event.builder()
                .id(UUID.randomUUID())
                .createdAt(now.minusHours(1))
                .build();

        Event event3 = Event.builder()
                .id(UUID.randomUUID())
                .createdAt(now)
                .build();

        events = Arrays.asList(event3, event2, event1);
        when(eventRepository.findAllByOrderByCreatedAtDesc()).thenReturn(events);

        // When
        List<Event> result = findAllEventsUseCase.execute();

        // Then
        assertThat(result).hasSize(3);
        verify(eventRepository, times(1)).findAllByOrderByCreatedAtDesc();
    }

    @Test
    @DisplayName("Deve retornar lista com um único evento")
    void deveRetornarListaComUmUnicoEvento() {
        // Given
        Event event = Event.builder()
                .id(UUID.randomUUID())
                .orderId(UUID.randomUUID())
                .createdAt(LocalDateTime.now())
                .build();

        events = Arrays.asList(event);
        when(eventRepository.findAllByOrderByCreatedAtDesc()).thenReturn(events);

        // When
        List<Event> result = findAllEventsUseCase.execute();

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0)).isEqualTo(event);
    }
}

