/*
 * Copyright OpenSearch Contributors
 * SPDX-License-Identifier: Apache-2.0
 */

package com.amazon.dataprepper.plugins.source;

import com.amazon.dataprepper.metrics.PluginMetrics;
import com.amazon.dataprepper.model.buffer.Buffer;
import com.amazon.dataprepper.model.event.Event;
import com.amazon.dataprepper.model.record.Record;
import com.amazon.dataprepper.plugins.source.codec.Codec;
import com.amazon.dataprepper.plugins.source.configuration.OnErrorOption;
import com.amazon.dataprepper.plugins.source.configuration.SqsOptions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.sqs.SqsClient;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.lessThanOrEqualTo;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SqsWorkerIT {
    private S3Client s3Client;
    private SqsClient sqsClient;
    private S3Service s3Service;
    private Buffer<Record<Event>> buffer;
    private S3SourceConfig s3SourceConfig;
    private S3ObjectGenerator s3ObjectGenerator;
    private String bucket;
    private Codec codec;
    private PluginMetrics pluginMetrics;

    @BeforeEach
    void setUp() {
        s3Client = S3Client.builder()
                .region(Region.of(System.getProperty("tests.s3source.region")))
                .build();
        bucket = System.getProperty("tests.s3source.bucket");
        s3ObjectGenerator = new S3ObjectGenerator(s3Client, bucket);


        sqsClient = SqsClient.builder()
                .region(Region.of(System.getProperty("tests.s3source.region")))
                .build();

        s3SourceConfig = mock(S3SourceConfig.class);
        buffer = mock(Buffer.class);
        codec = mock(Codec.class);
        pluginMetrics = mock(PluginMetrics.class);
        s3Service = mock(S3Service.class);

        final SqsOptions sqsOptions = mock(SqsOptions.class);
        when(sqsOptions.getSqsUrl()).thenReturn(System.getProperty("tests.s3source.queue.url"));
        when(sqsOptions.getVisibilityTimeout()).thenReturn(Duration.ofSeconds(60));
        when(sqsOptions.getMaximumMessages()).thenReturn(10);
        when(sqsOptions.getWaitTime()).thenReturn(Duration.ofSeconds(10));
        when(s3SourceConfig.getSqsOptions()).thenReturn(sqsOptions);
        when(s3SourceConfig.getOnErrorOption()).thenReturn(OnErrorOption.DELETE_MESSAGES);
    }

    private SqsWorker createObjectUnderTest() {
        return new SqsWorker(sqsClient, s3Service, s3SourceConfig);
    }

    @AfterEach
    void processRemainingMessages() {
        final SqsWorker objectUnderTest = createObjectUnderTest();
        int sqsMessagesProcessed;
        do {
            sqsMessagesProcessed = objectUnderTest.processSqsMessages();
        }
        while (sqsMessagesProcessed > 0);
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 5})
    void processSqsMessages_should_return_at_least_one_message(final int numberOfObjectsToWrite) throws IOException {
        writeToS3(numberOfObjectsToWrite);

        final SqsWorker objectUnderTest = createObjectUnderTest();
        int sqsMessagesProcessed = objectUnderTest.processSqsMessages();

        assertThat(sqsMessagesProcessed, greaterThanOrEqualTo(1));
        assertThat(sqsMessagesProcessed, lessThanOrEqualTo(numberOfObjectsToWrite));
    }

    @Test
    void processSqsMessages_should_return_zero_if_no_objects_are_written() {
        SqsWorker objectUnderTest = createObjectUnderTest();
        int sqsMessagesProcessed = objectUnderTest.processSqsMessages();

        assertThat(sqsMessagesProcessed, equalTo(0));
    }

    private void writeToS3(final int numberOfObjectsToWrite) throws IOException {
        final int numberOfRecords = 100;
        final NewlineDelimitedRecordsGenerator newlineDelimitedRecordsGenerator = new NewlineDelimitedRecordsGenerator();
        for (int i = 0; i < numberOfObjectsToWrite; i++) {
            final String key = "s3source/s3/" + numberOfRecords + "_" + Instant.now().toString() + newlineDelimitedRecordsGenerator.getFileExtension();
            s3ObjectGenerator.write(numberOfRecords, key, newlineDelimitedRecordsGenerator);
        }
    }
}
