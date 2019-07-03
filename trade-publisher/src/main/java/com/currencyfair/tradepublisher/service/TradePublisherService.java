package com.currencyfair.tradepublisher.service;

import com.currencyfair.tradepublisher.dto.TradeMessage;
import com.currencyfair.tradepublisher.validation.ValidationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class TradePublisherService {

    private SqsService sqsService;
    private String queueName;
    private ObjectMapper objectMapper;
    private static final Logger LOG = LoggerFactory.getLogger(SqsService.class);
    private static final Set<String> ISO_COUNTRIES = new HashSet<>
        (Arrays.asList(Locale.getISOCountries()));

    @Autowired
    public TradePublisherService(SqsService sqsService, @Value("${queue.name}") String queueName) {
        this.sqsService = sqsService;
        this.objectMapper = new ObjectMapper();
        this.queueName = queueName;
    }

    public void publishMessage(TradeMessage tradeMessage) {
        try {

            if (!ISO_COUNTRIES.contains(tradeMessage.getOriginatingCountry())) {
                throw new ValidationException("Invalid Country code");
            }
            try {
                Currency.getInstance(tradeMessage.getCurrencyFrom());
                Currency.getInstance(tradeMessage.getCurrencyTo());
            } catch (IllegalArgumentException e) {
                throw new ValidationException("Invalid Currency Code");
            }
            String message = objectMapper.writeValueAsString(tradeMessage);
            sqsService.sendMessage(message, queueName);
        } catch (JsonProcessingException e) {
            LOG.error("Unable to parse {}", e.getMessage());
        }
    }

}
