package com.nextime.order.infrastructure.adapters;

import com.nextime.order.application.gateways.EventPublisherPort;
import com.nextime.order.application.usecases.interfaces.event.SaveEventUseCase;
import com.nextime.order.infrastructure.messaging.producer.SagaProducer;
import com.nextime.order.infrastructure.persistence.document.Event;
import com.nextime.order.infrastructure.persistence.document.Order;
import com.nextime.order.utils.JsonConverter;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class EventPublisherAdapter implements EventPublisherPort {

    private final SagaProducer sagaProducer;
    private final JsonConverter jsonConverter;
    private final SaveEventUseCase saveEventUseCase;

    @Override
    public void publish(Order order) {

        final Event event = Event.builder()
                .orderId(order.getId())
                .transactionId(order.getTransactionId())
                .payload(order)
                .createdAt(LocalDateTime.now())
                .build();

        saveEventUseCase.execute(event);
        sagaProducer.sendMessage(jsonConverter.toJson(event));
    }
}
