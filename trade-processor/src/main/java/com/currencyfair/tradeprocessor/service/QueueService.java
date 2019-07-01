package com.currencyfair.tradeprocessor.service;

import com.currencyfair.tradeprocessor.model.TotalTransaction;
import com.currencyfair.tradeprocessor.model.TradeMessage;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Currency;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@Service
public class QueueService {

    private static final Logger log = LoggerFactory.getLogger(QueueService.class);
    private Map<Locale, TotalTransaction> totalTransactions = new HashMap<>();
    private ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private SimpMessagingTemplate template;

    public void handle(String message) {
        log.info("Handling started for queue one message: {}", message);
        try {
            TradeMessage tradeMessage = objectMapper.readValue(message, TradeMessage.class);
            calculate(tradeMessage);
            String countryWiseTransactions = objectMapper.writeValueAsString(totalTransactions);
            template.convertAndSend("/topic/trades", countryWiseTransactions);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void calculate(TradeMessage tradeMessage) {
        Locale originatingCountry = new Locale("EN", tradeMessage.getOriginatingCountry());
        Currency originatingCountryCurrency = Currency.getInstance(originatingCountry);
        Double purchaseOrSaleAmount = tradeMessage.getCurrencyFrom().getCurrencyCode().equals(originatingCountryCurrency.getCurrencyCode()) ?
            tradeMessage.getAmountBuy() :
            tradeMessage.getAmountSell();
        TotalTransaction totalTransaction;
        if (totalTransactions.containsKey(originatingCountry)) {
            totalTransaction = totalTransactions.get(originatingCountry);
            totalTransaction.add(purchaseOrSaleAmount);
        } else {
            totalTransaction = new TotalTransaction(originatingCountry, originatingCountryCurrency, purchaseOrSaleAmount);
        }
        totalTransactions.put(originatingCountry, totalTransaction);
    }
}
