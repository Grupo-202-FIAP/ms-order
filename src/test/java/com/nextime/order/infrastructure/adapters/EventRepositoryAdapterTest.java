package com.nextime.order.infrastructure.adapters;

import com.nextime.order.application.gateways.LoggerRepositoryPort;
import com.nextime.order.infrastructure.exception.RepositoryException;
import com.nextime.order.infrastructure.persistence.document.Event;
import com.nextime.order.infrastructure.persistence.repository.IEventRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataAccessException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EventRepositoryAdapterTest {

    @Mock
    private IEventRepository eventRepository;

    @Mock
    private LoggerRepositoryPort logger;

    @InjectMocks
    private EventRepositoryAdapter eventRepositoryAdapter;

    private Event event;
    private UUID orderId;
    private UUID transactionId;

    @BeforeEach
    void setUp() {
        orderId = UUID.randomUUID();
        transactionId = UUID.randomUUID();
        event = Event.builder()
                .id(UUID.randomUUID())
                .orderId(orderId)
                .transactionId(transactionId)
                .build();
    }

    @Test
    void shouldFindAllOrderedByCreationDateSuccessfully() {
        // Given
        Event event1 = Event.builder().id(UUID.randomUUID()).build();
        Event event2 = Event.builder().id(UUID.randomUUID()).build();
        List<Event> events = Arrays.asList(event1, event2);
        when(eventRepository.findAllByOrderByCreatedAtDesc()).thenReturn(events);

        // When
        List<Event> result = eventRepositoryAdapter.findAllOrderedByCreationDate();

        // Then
        assertThat(result).hasSize(2);
        assertThat(result).containsExactly(event1, event2);
        verify(logger).info(any(String.class));
        verify(eventRepository).findAllByOrderByCreatedAtDesc();
    }

    @Test
    void shouldThrowRepositoryExceptionWhenFindAllFails() {
        // Given
        DataAccessException exception = new DataAccessException("Database error") {};
        when(eventRepository.findAllByOrderByCreatedAtDesc()).thenThrow(exception);

        // When/Then
        assertThatThrownBy(() -> eventRepositoryAdapter.findAllOrderedByCreationDate())
                .isInstanceOf(RepositoryException.class)
                .hasMessageContaining("Erro ao acessar base de dados")
                .hasCause(exception);

        verify(logger).error(any(String.class), any(Exception.class));
    }

    @Test
    void shouldFindLatestByOrderIdSuccessfully() {
        // Given
        when(eventRepository.findTop1ByOrderIdOrderByCreatedAtDesc(orderId))
                .thenReturn(Optional.of(event));

        // When
        Optional<Event> result = eventRepositoryAdapter.findLatestByOrderId(orderId);

        // Then
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(event);
        verify(logger).info(any(String.class), any(UUID.class));
        verify(eventRepository).findTop1ByOrderIdOrderByCreatedAtDesc(orderId);
    }

    @Test
    void shouldThrowRepositoryExceptionWhenFindByOrderIdFails() {
        // Given
        DataAccessException exception = new DataAccessException("Database error") {};
        when(eventRepository.findTop1ByOrderIdOrderByCreatedAtDesc(orderId)).thenThrow(exception);

        // When/Then
        assertThatThrownBy(() -> eventRepositoryAdapter.findLatestByOrderId(orderId))
                .isInstanceOf(RepositoryException.class)
                .hasMessageContaining("Erro ao buscar evento por orderId")
                .hasCause(exception);

        verify(logger).error(any(String.class), any(Exception.class));
    }

    @Test
    void shouldFindLatestByTransactionIdSuccessfully() {
        // Given
        when(eventRepository.findTop1ByTransactionIdOrderByCreatedAtDesc(transactionId))
                .thenReturn(Optional.of(event));

        // When
        Optional<Event> result = eventRepositoryAdapter.findLatestByTransactionId(transactionId);

        // Then
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(event);
        verify(logger).info(any(String.class), any(UUID.class));
        verify(eventRepository).findTop1ByTransactionIdOrderByCreatedAtDesc(transactionId);
    }

    @Test
    void shouldThrowRepositoryExceptionWhenFindByTransactionIdFails() {
        // Given
        DataAccessException exception = new DataAccessException("Database error") {};
        when(eventRepository.findTop1ByTransactionIdOrderByCreatedAtDesc(transactionId))
                .thenThrow(exception);

        // When/Then
        assertThatThrownBy(() -> eventRepositoryAdapter.findLatestByTransactionId(transactionId))
                .isInstanceOf(RepositoryException.class)
                .hasMessageContaining("Erro ao buscar evento por transactionId")
                .hasCause(exception);

        verify(logger).error(any(String.class), any(Exception.class));
    }

    @Test
    void shouldSaveEventSuccessfully() {
        // Given
        event.setCreatedAt(null);
        Event savedEvent = Event.builder()
                .id(event.getId())
                .orderId(event.getOrderId())
                .transactionId(event.getTransactionId())
                .createdAt(LocalDateTime.now())
                .build();
        when(eventRepository.save(any(Event.class))).thenReturn(savedEvent);

        // When
        Event result = eventRepositoryAdapter.save(event);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getCreatedAt()).isNotNull();
        verify(logger).info(any(String.class), any(UUID.class), any(UUID.class));
        verify(eventRepository).save(any(Event.class));
    }

    @Test
    void shouldSaveEventWithExistingCreatedAt() {
        // Given
        LocalDateTime existingDate = LocalDateTime.now().minusDays(1);
        event.setCreatedAt(existingDate);
        when(eventRepository.save(event)).thenReturn(event);

        // When
        Event result = eventRepositoryAdapter.save(event);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getCreatedAt()).isEqualTo(existingDate);
        verify(eventRepository).save(event);
    }

    @Test
    void shouldThrowRepositoryExceptionWhenSaveFails() {
        // Given
        DataAccessException exception = new DataAccessException("Database error") {};
        when(eventRepository.save(any(Event.class))).thenThrow(exception);

        // When/Then
        assertThatThrownBy(() -> eventRepositoryAdapter.save(event))
                .isInstanceOf(RepositoryException.class)
                .hasMessageContaining("Erro ao salvar evento")
                .hasCause(exception);

        verify(logger).error(any(String.class), any(Exception.class));
    }
}



