package com.nextime.order.application.config.sqs;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;

import java.net.URI;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class SqsConfigTest {

    @InjectMocks
    private SqsConfig sqsConfig;

    @Test
    void shouldCreateSqsAsyncClientForLocalProfile() {
        // Given
        ReflectionTestUtils.setField(sqsConfig, "region", "us-east-1");
        String endpoint = "http://localhost:4566";

        // When
        SqsAsyncClient client = sqsConfig.sqsAsyncClientLocal(endpoint);

        // Then
        assertThat(client).isNotNull();
        assertThat(client.serviceName()).isEqualTo("sqs");
    }

    @Test
    void shouldCreateSqsAsyncClientForEksProfile() {
        // Given
        ReflectionTestUtils.setField(sqsConfig, "region", "us-east-1");

        // When
        SqsAsyncClient client = sqsConfig.sqsAsyncClientEks();

        // Then
        assertThat(client).isNotNull();
        assertThat(client.serviceName()).isEqualTo("sqs");
    }
}

