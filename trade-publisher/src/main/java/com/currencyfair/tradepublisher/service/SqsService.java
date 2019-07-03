package com.currencyfair.tradepublisher.service;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.*;
import com.currencyfair.tradepublisher.dto.TradeMessage;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Component
public class SqsService {
    private AmazonSQS sqsClient;
    private static final String QUEUE_NAME = "TradeQueue.fifo";
    private String queueUrl;
    private ObjectMapper objectMapper;
    private Boolean isLocalQueue;

    private static final Logger LOG = LoggerFactory.getLogger(SqsService.class);

    public SqsService(@Value("${aws.sqs.endpoint}") String endpoint,
                      @Value("${aws.region}") String region,
                      @Value("${aws.accessKey}") String accessKey,
                      @Value("${aws.secretKey}") String secretKey,
                      @Value("${local.queue}") Boolean isLocalQueue) {

        this.sqsClient = AmazonSQSClientBuilder.standard()
            .withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(accessKey, secretKey)))
            .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(endpoint, region))
            .build();
        try {
            if (isLocalQueue) {
                sqsClient.createQueue(QUEUE_NAME);
            }
        } catch (AmazonSQSException e) {
            if (!e.getErrorCode().equals("QueueAlreadyExists")) {
                LOG.error(e.getMessage());
                throw e;
            }
        }
        this.isLocalQueue = isLocalQueue;
        queueUrl = sqsClient.getQueueUrl(QUEUE_NAME).getQueueUrl();
        objectMapper = new ObjectMapper();
    }

    public void sendMessage(TradeMessage tradeMessage) {
        LOG.debug("Sending message {} to queue", tradeMessage);
        try {
            String message = objectMapper.writeValueAsString(tradeMessage);
            SendMessageRequest messageRequest = new SendMessageRequest()
                .withQueueUrl(queueUrl)
                .withMessageBody(message);
            if (!isLocalQueue) {
                messageRequest.withMessageGroupId("TradeGroupId")
                    .withMessageDeduplicationId(UUID.randomUUID().toString());
            }
            sqsClient.sendMessage(messageRequest);
        } catch (JsonProcessingException e) {
            LOG.error("Unable to parse {}", e.getMessage());
        }
    }

}
