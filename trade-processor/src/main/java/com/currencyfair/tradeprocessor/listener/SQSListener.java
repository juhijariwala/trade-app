//package com.currencyfair.tradeprocessor.listener;
//
//import com.amazonaws.auth.AWSStaticCredentialsProvider;
//import com.amazonaws.auth.BasicAWSCredentials;
//import com.amazonaws.client.builder.AwsClientBuilder;
//import com.amazonaws.services.sqs.AmazonSQS;
//import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
//import com.amazonaws.services.sqs.model.Message;
//import com.amazonaws.services.sqs.model.ReceiveMessageRequest;
//import com.amazonaws.services.sqs.model.SetQueueAttributesRequest;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.boot.context.event.ApplicationReadyEvent;
//import org.springframework.context.event.ContextStartedEvent;
//import org.springframework.context.event.EventListener;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Component;
//
//import java.util.List;
//
//
//@Component
//public class SQSListener {
//    private static final Logger log = LoggerFactory.getLogger(SQSListener.class);
//
//    private AmazonSQS sqsClient;
//    private static final String QUEUE_NAME = "trade";
//
//    public SQSListener(@Value("${aws.sqs.endpoint}") String endpoint,
//                       @Value("${aws.region}") String region,
//                       @Value("${aws.accessKey}") String accessKey,
//                       @Value("${aws.secretKey}") String secretKey) {
//        this.sqsClient = AmazonSQSClientBuilder.standard()
//            .withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(accessKey, secretKey)))
//            .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(endpoint, region))
//            .build();
//        String queueUrl = sqsClient.getQueueUrl(QUEUE_NAME).getQueueUrl();
//        SetQueueAttributesRequest set_attrs_request = new SetQueueAttributesRequest()
//            .withQueueUrl(queueUrl)
//            .addAttributesEntry("ReceiveMessageWaitTimeSeconds", "20");
//        sqsClient.setQueueAttributes(set_attrs_request);
//
//    }
//
//    @EventListener(ApplicationReadyEvent.class)
//    public void initialize() {
//        {
//
////            while (true) {
////
////                log.info("Receiving messages from MyQueue.\n");
////                final ReceiveMessageRequest receiveMessageRequest =
////                    new ReceiveMessageRequest(queueUrl)
////                        .withMaxNumberOfMessages(1)
////                        .withWaitTimeSeconds(3);
////                final List<Message> messages = sqsClient.receiveMessage(receiveMessageRequest)
////                    .getMessages();
////                for (final com.amazonaws.services.sqs.model.Message message : messages) {
////                    log.info("Message received {}", messages);
////                }
////
////            }
//        }
//    }
//
//}
