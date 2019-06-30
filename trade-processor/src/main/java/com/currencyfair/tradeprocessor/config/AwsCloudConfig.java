package com.currencyfair.tradeprocessor.config;

import com.amazonaws.auth.*;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.sqs.AmazonSQSAsync;
import com.amazonaws.services.sqs.AmazonSQSAsyncClientBuilder;
import com.currencyfair.tradeprocessor.service.QueueService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.cloud.aws.messaging.config.SimpleMessageListenerContainerFactory;
import org.springframework.cloud.aws.messaging.config.annotation.SqsClientConfiguration;
import org.springframework.cloud.aws.messaging.config.annotation.SqsConfiguration;
import org.springframework.cloud.aws.messaging.listener.QueueMessageHandler;
import org.springframework.context.annotation.*;

@Configuration
@Import({SqsClientConfiguration.class, SqsConfiguration.class})
public class AwsCloudConfig {
    private static final Logger log = LoggerFactory.getLogger(AwsCloudConfig.class);

    @Bean
    public AWSCredentialsProvider awsCredentialsProvider(@Value("${aws.sqs.accessKeyId}") String accessKey, @Value("${aws.sqs.secretAccessKey}") String secretKey) {
        return new AWSStaticCredentialsProvider(new BasicAWSCredentials(accessKey, secretKey));
    }

    @Bean
    @Primary
    @Qualifier("amazonSQSAsync")
    public AmazonSQSAsync amazonSQSAsync(@Value("${aws.sqs.endpoint}") String endpoint, @Value("${cloud.aws.region.static}") String region, AWSCredentialsProvider awsCredentialsProvider) {
        return AmazonSQSAsyncClientBuilder.standard()
            .withCredentials(awsCredentialsProvider)
            .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(endpoint, region))
            .build();
    }

    @Bean
    @Primary
    public SimpleMessageListenerContainerFactory simpleMessageListenerContainerFactory(AmazonSQSAsync amazonSqs) {
        SimpleMessageListenerContainerFactory factory = new SimpleMessageListenerContainerFactory();
        factory.setAmazonSqs(amazonSqs);
        factory.setMaxNumberOfMessages(1);
        factory.setWaitTimeOut(10);
        factory.setQueueMessageHandler(new QueueMessageHandler());
        return factory;
    }
}
