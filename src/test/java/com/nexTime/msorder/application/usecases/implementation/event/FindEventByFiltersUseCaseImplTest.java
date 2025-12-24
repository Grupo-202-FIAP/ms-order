package com.nexTime.msorder.application.usecases.implementation.event;

import com.nextime.order.application.usecases.implementation.event.FindEventByFiltersUseCaseImpl;
import com.nextime.order.domain.EventFilters;
import com.nextime.order.domain.exception.event.EventNotFoundOrderIdException;
import com.nextime.order.domain.exception.event.EventNotFoundTransactionIdException;
import com.nextime.order.infrastructure.persistence.document.Event;
import com.nextime.order.infrastructure.persistence.document.Order;
import com.nextime.order.infrastructure.persistence.repository.IEventRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes para FindEventByFiltersUseCaseImpl")
class FindEventByFiltersUseCaseImplTest {

    @Mock
    private IEventRepository eventRepository;

    @InjectMocks
    private FindEventByFiltersUseCaseImpl findEventByFiltersUseCase;

    private UUID orderId;
    private UUID transactionId;
    private Event event;

    @BeforeEach
    void setUp() {
        orderId = UUID.randomUUID();
        transactionId = UUID.randomUUID();
        event = Event.builder()
                .id(UUID.randomUUID())
                .orderId(orderId)
                .transactionId(transactionId)
                .payload(Order.builder().id(orderId).build())
                .createdAt(LocalDateTime.now())
                .build();
    }

    @Test
    @DisplayName("Deve encontrar evento por orderId com sucesso")
    void deveEncontrarEventoPorOrderIdComSucesso() {
        // Given
        EventFilters filter = new EventFilters();
        filter.setOrderId(orderId);
        filter.setTransactionId(null);

        when(eventRepository.findTop1ByOrderIdOrderByCreatedAtDesc(orderId))
                .thenReturn(Optional.of(event));

        // When
        Event result = findEventByFiltersUseCase.execute(filter);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getOrderId()).isEqualTo(orderId);
        verify(eventRepository, times(1)).findTop1ByOrderIdOrderByCreatedAtDesc(orderId);
        verify(eventRepository, never()).findTop1ByTransactionIdOrderByCreatedAtDesc(any());
    }

    @Test
    @DisplayName("Deve encontrar evento por transactionId com sucesso")
    void deveEncontrarEventoPorTransactionIdComSucesso() {
        // Given
        EventFilters filter = new EventFilters();
        filter.setOrderId(null);
        filter.setTransactionId(transactionId);

        when(eventRepository.findTop1ByTransactionIdOrderByCreatedAtDesc(transactionId))
                .thenReturn(Optional.of(event));

        // When
        Event result = findEventByFiltersUseCase.execute(filter);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getTransactionId()).isEqualTo(transactionId);
        verify(eventRepository, times(1)).findTop1ByTransactionIdOrderByCreatedAtDesc(transactionId);
        verify(eventRepository, never()).findTop1ByOrderIdOrderByCreatedAtDesc(any());
    }

    @Test
    @DisplayName("Deve priorizar orderId quando ambos os filtros estiverem presentes")
    void devePriorizarOrderIdQuandoAmbosFiltrosPresentes() {
        // Given
        EventFilters filter = new EventFilters();
        filter.setOrderId(orderId);
        filter.setTransactionId(transactionId);

        when(eventRepository.findTop1ByOrderIdOrderByCreatedAtDesc(orderId))
                .thenReturn(Optional.of(event));

        // When
        Event result = findEventByFiltersUseCase.execute(filter);

        // Then
        assertThat(result).isNotNull();
        verify(eventRepository, times(1)).findTop1ByOrderIdOrderByCreatedAtDesc(orderId);
        verify(eventRepository, never()).findTop1ByTransactionIdOrderByCreatedAtDesc(any());
    }

    @Test
    @DisplayName("Deve lançar exceção quando evento não for encontrado por orderId")
    void deveLancarExcecaoQuandoEventoNaoEncontradoPorOrderId() {
        // Given
        EventFilters filter = new EventFilters();
        filter.setOrderId(orderId);
        filter.setTransactionId(null);

        when(eventRepository.findTop1ByOrderIdOrderByCreatedAtDesc(orderId))
                .thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> findEventByFiltersUseCase.execute(filter))
                .isInstanceOf(EventNotFoundOrderIdException.class)
                .hasMessageContaining("Não foi encontrado um evento com o order id:" + orderId);

        verify(eventRepository, times(1)).findTop1ByOrderIdOrderByCreatedAtDesc(orderId);
    }

    @Test
    @DisplayName("Deve lançar exceção quando evento não for encontrado por transactionId")
    void deveLancarExcecaoQuandoEventoNaoEncontradoPorTransactionId() {
        // Given
        EventFilters filter = new EventFilters();
        filter.setOrderId(null);
        filter.setTransactionId(transactionId);

        when(eventRepository.findTop1ByTransactionIdOrderByCreatedAtDesc(transactionId))
                .thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> findEventByFiltersUseCase.execute(filter))
                .isInstanceOf(EventNotFoundTransactionIdException.class)
                .hasMessageContaining("Não foi encontrado um evento com o transaction ID: " + transactionId);

        verify(eventRepository, times(1)).findTop1ByTransactionIdOrderByCreatedAtDesc(transactionId);
    }

    @Test
    @DisplayName("Deve lançar exceção quando ambos os filtros estiverem vazios")
    void deveLancarExcecaoQuandoAmbosFiltrosVazios() {
        // Given
        EventFilters filter = new EventFilters();
        filter.setOrderId(null);
        filter.setTransactionId(null);

        // When & Then
        assertThatThrownBy(() -> findEventByFiltersUseCase.execute(filter))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Filtros inválidos: é necessário fornecer um orderId ou transactionId");

        verify(eventRepository, never()).findTop1ByOrderIdOrderByCreatedAtDesc(any());
        verify(eventRepository, never()).findTop1ByTransactionIdOrderByCreatedAtDesc(any());
    }
}

