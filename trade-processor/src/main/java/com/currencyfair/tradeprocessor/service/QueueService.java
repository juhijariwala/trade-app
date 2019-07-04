package com.currencyfair.tradeprocessor.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class QueueService {

    private static final Logger log = LoggerFactory.getLogger(QueueService.class);

    @Autowired
    private TransactionAnalyzer transactionAnalyzer;

    public void handle(String message) {
        log.info("Handling started for queue one message: {}", message);
        transactionAnalyzer.generateAndPush(message);
    }
}
