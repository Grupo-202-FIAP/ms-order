package integration.consumer;

import java.util.List;
import java.util.stream.Collectors;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.Message;
import software.amazon.awssdk.services.sqs.model.ReceiveMessageRequest;

public class ConsumeMessage {
    public List<String> receiveMessages(
            SqsClient sqsClient,
            String queueUrl,
            int maxMessages
    ) {

        ReceiveMessageRequest request = ReceiveMessageRequest.builder()
                .queueUrl(queueUrl)
                .maxNumberOfMessages(maxMessages)
                .waitTimeSeconds(1)
                .visibilityTimeout(0)
                .build();

        return sqsClient.receiveMessage(request)
                .messages()
                .stream()
                .map(Message::body)
                .collect(Collectors.toList());
    }
}
