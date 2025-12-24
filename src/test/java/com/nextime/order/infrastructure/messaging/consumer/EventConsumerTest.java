package com.nextime.order.infrastructure.messaging.consumer;

import com.nextime.order.application.gateways.LoggerPort;
import com.nextime.order.infrastructure.persistence.document.Event;
import com.nextime.order.infrastructure.persistence.document.Order;
import com.nextime.order.utils.JsonConverter;
import java.time.LocalDateTime;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes para EventConsumer")
class EventConsumerTest {

    @Mock
    private LoggerPort logger;

    @Mock
    private JsonConverter jsonConverter;

    @InjectMocks
    private EventConsumer eventConsumer;

    private String payload;
    private Event event;

    @BeforeEach
    void setUp() {
        UUID orderId = UUID.randomUUID();
        event = Event.builder()
                .id(UUID.randomUUID())
                .orderId(orderId)
                .transactionId(UUID.randomUUID())
                .payload(Order.builder().id(orderId).build())
                .createdAt(LocalDateTime.now())
                .build();

        payload = "{\"id\":\"" + event.getId() + "\",\"orderId\":\"" + orderId + "\"}";
    }
}

