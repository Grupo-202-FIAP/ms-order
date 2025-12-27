package com.nextime.order.infrastructure.messaging.producer;

import com.nextime.order.application.exception.MessagePublishException;
import com.nextime.order.application.gateways.LoggerRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;
import software.amazon.awssdk.services.sqs.model.SendMessageResponse;
import java.lang.reflect.Field;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class SagaProducerTest {

    @Mock
    private LoggerRepositoryPort logger;

    @Mock
    private SqsAsyncClient sqsAsyncClient;

    @InjectMocks
    private SagaProducer sagaProducer;

    private String payload;

    @BeforeEach
    void setUp() throws Exception {
        payload = "{\"id\":\"123\"}";
        // Set orderQueue field using reflection
        Field orderQueueField = SagaProducer.class.getDeclaredField("orderQueue");
        orderQueueField.setAccessible(true);
        orderQueueField.set(sagaProducer, "test-queue-url");
    }

    @Test
    void shouldSendMessageSuccessfully() {
        // Given
        SendMessageResponse response = SendMessageResponse.builder()
                .messageId("test-message-id")
                .build();
        CompletableFuture<SendMessageResponse> future = CompletableFuture.completedFuture(response);
        doAnswer(invocation -> {
            @SuppressWarnings("unchecked")
            Consumer<SendMessageRequest.Builder> consumer = invocation.getArgument(0);
            consumer.accept(SendMessageRequest.builder());
            return future;
        }).when(sqsAsyncClient).sendMessage(isA(Consumer.class));

        // When
        sagaProducer.sendMessage(payload);

        // Then
        verify(logger).info(any(String.class), any(), any(String.class));
        verify(sqsAsyncClient).sendMessage(isA(Consumer.class));
    }

    @Test
    void shouldThrowMessagePublishExceptionWhenSendFails() {
        // Given
        RuntimeException exception = new RuntimeException("SQS error");
        doThrow(exception).when(sqsAsyncClient).sendMessage(isA(Consumer.class));

        // When/Then
        assertThatThrownBy(() -> sagaProducer.sendMessage(payload))
                .isInstanceOf(MessagePublishException.class)
                .hasMessageContaining("Erro ao publicar mensagem na fila")
                .hasCause(exception);

        verify(logger).error(any(String.class), any(String.class), any(Exception.class));
    }
}

