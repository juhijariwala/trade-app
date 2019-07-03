package com.currencyfair.tradepublisher.service;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.AmazonSQSException;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class SqsService {
    private AmazonSQS sqsClient;
    private Boolean isLocalQueue;

    private static final Logger LOG = LoggerFactory.getLogger(SqsService.class);

    public SqsService(@Value("${aws.sqs.endpoint}") String endpoint,
                      @Value("${aws.region}") String region,
                      @Value("${aws.accessKey}") String accessKey,
                      @Value("${aws.secretKey}") String secretKey,
                      @Value("${local.queue}") Boolean isLocalQueue,
                      @Value("${queue.name}") String queueName) {

        this.sqsClient = AmazonSQSClientBuilder.standard()
            .withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(accessKey, secretKey)))
            .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(endpoint, region))
            .build();
        try {
            if (isLocalQueue) {
                sqsClient.createQueue(queueName);
            }
        } catch (AmazonSQSException e) {
            if (!e.getErrorCode().equals("QueueAlreadyExists")) {
                LOG.error(e.getMessage());
                throw e;
            }
        }
        this.isLocalQueue = isLocalQueue;
    }

    public void sendMessage(String message, String queueName) {
        String queueUrl = sqsClient.getQueueUrl(queueName).getQueueUrl();
        LOG.debug("Sending message {} to queue {}", message, queueName);

        SendMessageRequest messageRequest = new SendMessageRequest()
            .withQueueUrl(queueUrl)
            .withMessageBody(message);
        if (!isLocalQueue) {
            messageRequest.withMessageGroupId("TradeGroupId")
                .withMessageDeduplicationId(UUID.randomUUID().toString());
        }
        sqsClient.sendMessage(messageRequest);
    }

}
