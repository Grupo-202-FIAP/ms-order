package com.nextime.order.infrastructure.messaging.producer;

import com.nextime.order.application.exception.MessagePublishException;
import com.nextime.order.application.gateways.LoggerRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;

@RequiredArgsConstructor
@Component
public class SagaProducer {

    private final LoggerRepositoryPort logger;

    private final SqsAsyncClient sqsAsyncClient;

    @Value("spring.cloud.aws.sqs.queue.order-queue")
    private String orderQueue;

    public void sendMessage(String payload) {

        logger.info("[sendMessage] Enviando mensagem para a fila: {} com valores: {}", orderQueue, payload);

        try {
            sqsAsyncClient.sendMessage(builder ->
                    builder.queueUrl(orderQueue)
                            .messageBody(payload)
            );

        } catch (Exception ex) {
            logger.error("[sendMessage] Falha ao enviar mensagem para a fila: {} com valores: {}", orderQueue, ex);

            throw new MessagePublishException(
                    "Erro ao publicar mensagem na fila",
                    ex
            );
        }
    }

}
