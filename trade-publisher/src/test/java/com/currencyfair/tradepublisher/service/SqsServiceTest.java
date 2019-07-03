package com.currencyfair.tradepublisher.service;

import akka.stream.BindFailedException;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;
import com.amazonaws.services.sqs.model.ReceiveMessageResult;
import org.elasticmq.rest.sqs.SQSRestServer;
import org.elasticmq.rest.sqs.SQSRestServerBuilder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class SqsServiceTest {
    private static final String QUEUE_NAME = "TradeQueue";
    private static final int SQS_PORT = 9324;
    private static final String SQS_HOSTNAME = "localhost";
    private SQSRestServer sqsRestServer;
    private SqsService sqsService;
    private AmazonSQS sqsClient;

    @Before
    public void setUp() {
        try {
            sqsRestServer = SQSRestServerBuilder
                .withPort(SQS_PORT)
                .withInterface(SQS_HOSTNAME)
                .start();
        } catch (BindFailedException e) {
            e.printStackTrace();
        }


        String enpoint = "http://" + SQS_HOSTNAME + ":" + SQS_PORT;

        String accessKey = "accessKey";
        String secretKey = "secretKey";
        String regionName = "regionName";
        sqsClient = AmazonSQSClientBuilder.standard()
            .withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(accessKey, secretKey)))
            .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(enpoint, regionName))
            .build();
        sqsService = new SqsService(enpoint, regionName, accessKey, secretKey, true, QUEUE_NAME);
    }

    @After
    public void tearDown() {
        if (sqsRestServer != null)
            sqsRestServer.stopAndWait();
    }

    @Test
    public void shouldSendMessageToQueue() throws Exception {
        String tradeMessage = "{\"userId\": \"134256\", \"currencyFrom\": \"EUR\", \"currencyTo\": \"GBP\",\n" +
            "\"amountSell\": 1000, \"amountBuy\": 747.10, \"rate\": 0.7471,\n" +
            "\"timePlaced\" : \"24-JAN-18 10:27:44\", \"originatingCountry\" : \"FR\"}";


        sqsService.sendMessage(tradeMessage, QUEUE_NAME);

        final ReceiveMessageRequest receiveMessageRequest =
            new ReceiveMessageRequest(sqsClient.getQueueUrl(QUEUE_NAME).getQueueUrl())
                .withMaxNumberOfMessages(1);
        ReceiveMessageResult receiveMessageResult = sqsClient.receiveMessage(receiveMessageRequest);

        String actualResponse = receiveMessageResult.getMessages().get(0).getBody();
        assertThat(actualResponse, is(tradeMessage));
    }
}
