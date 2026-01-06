package com.nextime.order.application.usecases.implementation.event;

import com.nextime.order.application.exception.InvalidEventFilterException;
import com.nextime.order.application.gateways.EventRepositoryPort;
import com.nextime.order.domain.EventFilters;
import com.nextime.order.domain.exception.event.EventNotFoundOrderIdException;
import com.nextime.order.domain.exception.event.EventNotFoundTransactionIdException;
import com.nextime.order.infrastructure.persistence.document.Event;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Optional;
import java.util.UUID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FindEventByFiltersUseCaseImplTest {

    @Mock
    private EventRepositoryPort eventRepository;

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
                .build();
    }

    @Test
    void shouldFindEventByOrderIdSuccessfully() {
        // Given
        EventFilters filters = new EventFilters(orderId, null);
        when(eventRepository.findLatestByOrderId(orderId)).thenReturn(Optional.of(event));

        // When
        Event result = findEventByFiltersUseCase.execute(filters);

        // Then
        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(event);
    }

    @Test
    void shouldFindEventByTransactionIdSuccessfully() {
        // Given
        EventFilters filters = new EventFilters(null, transactionId);
        when(eventRepository.findLatestByTransactionId(transactionId)).thenReturn(Optional.of(event));

        // When
        Event result = findEventByFiltersUseCase.execute(filters);

        // Then
        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(event);
    }

    @Test
    void shouldThrowInvalidEventFilterExceptionWhenFiltersIsNull() {
        // When/Then
        assertThatThrownBy(() -> findEventByFiltersUseCase.execute(null))
                .isInstanceOf(InvalidEventFilterException.class)
                .hasMessageContaining("É necessário informar orderId ou transactionId");
    }

    @Test
    void shouldThrowInvalidEventFilterExceptionWhenFiltersIsEmpty() {
        // Given
        EventFilters filters = new EventFilters(null, null);

        // When/Then
        assertThatThrownBy(() -> findEventByFiltersUseCase.execute(filters))
                .isInstanceOf(InvalidEventFilterException.class)
                .hasMessageContaining("É necessário informar orderId ou transactionId");
    }

    @Test
    void shouldThrowEventNotFoundOrderIdExceptionWhenOrderNotFound() {
        // Given
        EventFilters filters = new EventFilters(orderId, null);
        when(eventRepository.findLatestByOrderId(orderId)).thenReturn(Optional.empty());

        // When/Then
        assertThatThrownBy(() -> findEventByFiltersUseCase.execute(filters))
                .isInstanceOf(EventNotFoundOrderIdException.class);
    }

    @Test
    void shouldThrowEventNotFoundTransactionIdExceptionWhenTransactionNotFound() {
        // Given
        EventFilters filters = new EventFilters(null, transactionId);
        when(eventRepository.findLatestByTransactionId(transactionId)).thenReturn(Optional.empty());

        // When/Then
        assertThatThrownBy(() -> findEventByFiltersUseCase.execute(filters))
                .isInstanceOf(EventNotFoundTransactionIdException.class);
    }
}



