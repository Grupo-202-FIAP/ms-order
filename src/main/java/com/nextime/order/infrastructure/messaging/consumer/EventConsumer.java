package com.nextime.order.infrastructure.messaging.consumer;

import com.nextime.order.application.gateways.LoggerPort;
import com.nextime.order.application.usecases.interfaces.event.NotifyEventUseCase;
import com.nextime.order.utils.JsonConverter;
import io.awspring.cloud.sqs.annotation.SqsListener;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class EventConsumer {

    private final LoggerPort logger;
    private final JsonConverter jsonConverter;
    private final NotifyEventUseCase notifyEventUseCase;

    @SqsListener("${spring.sqs.queues.order-callback-queue}")
    public void consumeMessage(String payload) {
        try {
            logger.info("[consumeMessage] Consumindo mensagem da fila de callback: {}", payload);
            final var event = jsonConverter.toEvent(payload);
            notifyEventUseCase.execute(event);
            logger.info("[consumeMessage] Mensagem consumida da fila de callback: {}", event);
        } catch (Exception e) {
            logger.error("[consumeMessage] Erro ao consumir mensagem da fila de callback com a mensagem: {}", e.getMessage());
        }
    }

}
