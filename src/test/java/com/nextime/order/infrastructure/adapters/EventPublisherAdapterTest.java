package com.nextime.order.infrastructure.adapters;

import com.nextime.order.application.usecases.interfaces.event.SaveEventUseCase;
import com.nextime.order.infrastructure.messaging.producer.SagaProducer;
import com.nextime.order.infrastructure.persistence.document.Event;
import com.nextime.order.infrastructure.persistence.document.Order;
import com.nextime.order.utils.JsonConverter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.UUID;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EventPublisherAdapterTest {

    @Mock
    private SagaProducer sagaProducer;

    @Mock
    private JsonConverter jsonConverter;

    @Mock
    private SaveEventUseCase saveEventUseCase;

    @InjectMocks
    private EventPublisherAdapter eventPublisherAdapter;

    private Order order;
    private Event event;
    private String jsonEvent;

    @BeforeEach
    void setUp() {
        UUID orderId = UUID.randomUUID();
        UUID transactionId = UUID.randomUUID();
        order = Order.builder()
                .id(orderId)
                .transactionId(transactionId)
                .build();

        event = Event.builder()
                .id(UUID.randomUUID())
                .orderId(orderId)
                .transactionId(transactionId)
                .build();

        jsonEvent = "{\"id\":\"123\"}";
    }

    @Test
    void shouldPublishEventSuccessfully() {
        // Given
        when(saveEventUseCase.execute(any(Event.class))).thenReturn(event);
        when(jsonConverter.toJson(any(Event.class))).thenReturn(jsonEvent);

        // When
        eventPublisherAdapter.publish(order);

        // Then
        verify(saveEventUseCase).execute(any(Event.class));
        verify(jsonConverter).toJson(any(Event.class));
        verify(sagaProducer).sendMessage(jsonEvent);
    }
}

