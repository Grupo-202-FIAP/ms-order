package com.nexTime.order.infrastructure.messaging.consumer;

import com.nexTime.order.application.gateways.LoggerPort;
import com.nexTime.order.infrastructure.adapters.EventAdapter;
import com.nexTime.order.utils.JsonConverter;
import io.awspring.cloud.sqs.annotation.SqsListener;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class EventConsumer {

    private final LoggerPort logger;
    private final JsonConverter jsonConverter;
    private final EventAdapter eventAdapter;

    @SqsListener("${spring.sqs.queues.order-callback-queue}")
    public void consumeMessage(String payload) {
        try {
            logger.info("[consumeMessage] Consumindo mensagem da fila de callback: {}", payload);
            var event = jsonConverter.toEvent(payload);
            eventAdapter.notifyEvent(event);
            logger.info("[consumeMessage] Mensagem consumida da fila de callback: {}", event);
        } catch (Exception e) {
            logger.error("[consumeMessage] Erro ao consumir mensagem da fila de callback com a mensagem: {}", e.getMessage());
        }
    }

}
