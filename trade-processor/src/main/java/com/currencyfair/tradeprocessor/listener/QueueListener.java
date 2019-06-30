package com.currencyfair.tradeprocessor.listener;

import com.currencyfair.tradeprocessor.service.QueueService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.aws.messaging.listener.Acknowledgment;
import org.springframework.cloud.aws.messaging.listener.SqsMessageDeletionPolicy;
import org.springframework.cloud.aws.messaging.listener.annotation.SqsListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutionException;

@Component
@EnableAsync
public class QueueListener {

    public static final String QUEUE_NAME = "trade";
    private final QueueService oneService;
    private static final Logger log = LoggerFactory.getLogger(QueueListener.class);


    public QueueListener(final QueueService queueService) {
        this.oneService = queueService;
    }

    @Async
    @SqsListener(value = QUEUE_NAME, deletionPolicy = SqsMessageDeletionPolicy.NEVER)
    public void listen(Acknowledgment acknowledgment, String message, String messageId) {
        log.info("Queue One message received message : {}, messageId: {} ", message, messageId);
        oneService.handle(message);
        try {
            acknowledgment.acknowledge().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }
}
