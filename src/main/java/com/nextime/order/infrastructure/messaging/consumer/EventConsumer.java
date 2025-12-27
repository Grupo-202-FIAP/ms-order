package com.nextime.order.infrastructure.messaging.consumer;

import com.nextime.order.application.exception.JsonConversionException;
import com.nextime.order.application.exception.UseCaseExecutionException;
import com.nextime.order.application.gateways.LoggerRepositoryPort;
import com.nextime.order.application.usecases.interfaces.event.NotifyEventUseCase;
import com.nextime.order.infrastructure.exception.RepositoryException;
import com.nextime.order.utils.JsonConverter;
import io.awspring.cloud.sqs.annotation.SqsListener;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class EventConsumer {

    private final LoggerRepositoryPort logger;
    private final JsonConverter jsonConverter;
    private final NotifyEventUseCase notifyEventUseCase;

    @SqsListener("${spring.sqs.queues.order-callback-queue}")
    public void consumeMessage(String payload) {

        logger.info(
                "[EventConsumer.consumeMessage] Mensagem recebida da fila: {}",
                payload
        );

        try {
            final var event = jsonConverter.toEvent(payload);
            notifyEventUseCase.execute(event);
            logger.info(
                    "[EventConsumer.consumeMessage] Mensagem processada com sucesso: {}",
                    event
            );

        } catch (JsonConversionException ex) {
            logger.error(
                    "[EventConsumer.consumeMessage] Payload inválido. Mensagem descartada.",
                    ex
            );

        } catch (RepositoryException | UseCaseExecutionException ex) {
            logger.error(
                    "[EventConsumer.consumeMessage] Erro temporário. Retentando mensagem.",
                    ex
            );
            throw ex;

        } catch (Exception ex) {
            logger.error(
                    "[EventConsumer.consumeMessage] Erro inesperado ao processar mensagem.",
                    ex
            );
            throw ex;
        }
    }

}
