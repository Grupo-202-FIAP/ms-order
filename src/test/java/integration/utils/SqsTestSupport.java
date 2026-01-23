package integration.utils;

import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.*;

import java.util.HashMap;
import java.util.Map;

@Component
public class SqsTestSupport {

    private final SqsClient sqsClient;
    private final Map<String, String> queueUrlCache = new HashMap<>();

    public SqsTestSupport(SqsClient sqsClient) {
        this.sqsClient = sqsClient;
    }

    public String createQueue(String queueName) {
        try {
            CreateQueueRequest request = CreateQueueRequest.builder()
                    .queueName(queueName)
                    .build();
            String queueUrl = sqsClient.createQueue(request).queueUrl();
            queueUrlCache.put(queueName, queueUrl);
            return queueUrl;
        } catch (QueueNameExistsException e) {
            return resolveQueueUrl(queueName);
        }
    }

    public String resolveQueueUrl(String queueName) {
        if (queueUrlCache.containsKey(queueName)) {
            return queueUrlCache.get(queueName);
        }
        try {
            String queueUrl = sqsClient.getQueueUrl(
                    GetQueueUrlRequest.builder()
                            .queueName(queueName)
                            .build()
            ).queueUrl();
            queueUrlCache.put(queueName, queueUrl);
            return queueUrl;
        } catch (QueueDoesNotExistException e) {
            return createQueue(queueName);
        }
    }

    public void purgeQueue(String queueName) {
        String queueUrl = resolveQueueUrl(queueName);
        try {
            sqsClient.purgeQueue(PurgeQueueRequest.builder()
                    .queueUrl(queueUrl)
                    .build());
        } catch (Exception e) {
            // Ignore - queue might be empty
        }
    }

    public void sendMessage(String queueName, String messageBody) {
        String queueUrl = resolveQueueUrl(queueName);
        sqsClient.sendMessage(SendMessageRequest.builder()
                .queueUrl(queueUrl)
                .messageBody(messageBody)
                .build());
    }

    public int getApproximateMessageCount(String queueName) {
        String queueUrl = resolveQueueUrl(queueName);
        GetQueueAttributesResponse response = sqsClient.getQueueAttributes(
                GetQueueAttributesRequest.builder()
                        .queueUrl(queueUrl)
                        .attributeNames(QueueAttributeName.APPROXIMATE_NUMBER_OF_MESSAGES)
                        .build()
        );
        String count = response.attributes().get(QueueAttributeName.APPROXIMATE_NUMBER_OF_MESSAGES);
        return count != null ? Integer.parseInt(count) : 0;
    }
}