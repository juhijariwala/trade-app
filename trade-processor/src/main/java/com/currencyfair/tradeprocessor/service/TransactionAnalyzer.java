package com.currencyfair.tradeprocessor.service;

import com.currencyfair.tradeprocessor.model.TradeMessage;
import com.currencyfair.tradeprocessor.model.AggregatedTransaction;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@Service
public class TransactionAnalyzer {
    public static final String DESTINATION = "/topic/trades";
    private Map<String, AggregatedTransaction> transactionReport = new HashMap<>();
    private ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    private SimpMessagingTemplate template;

    public void generateAndPush(String message) {

        try {
            TradeMessage tradeMessage = objectMapper.readValue(message, TradeMessage.class);
            calculate(tradeMessage);
            String countryWiseTransactions = objectMapper.writeValueAsString(transactionReport);
            template.convertAndSend(DESTINATION, countryWiseTransactions);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void calculate(TradeMessage tradeMessage) {
        Locale originatingCountry = new Locale("EN", tradeMessage.getOriginatingCountry());
        String currencyPair = String.format("%s/%s", tradeMessage.getCurrencyFrom(), tradeMessage.getCurrencyTo());
        AggregatedTransaction aggregatedTransaction;

        if (transactionReport.containsKey(currencyPair)) {
            aggregatedTransaction = transactionReport.get(currencyPair);
            aggregatedTransaction.add(tradeMessage.getAmountSell(), tradeMessage.getAmountBuy());
            aggregatedTransaction.setMaxRate(tradeMessage.getRate());
            aggregatedTransaction.setMinRate(tradeMessage.getRate());
        } else {
            aggregatedTransaction = new AggregatedTransaction(originatingCountry,
                currencyPair,
                tradeMessage.getTimePlaced(),
                tradeMessage.getTimePlaced(),
                tradeMessage.getAmountSell(),
                tradeMessage.getAmountBuy(),
                BigInteger.ONE,
                tradeMessage.getRate(),
                tradeMessage.getRate()
            );
        }

        transactionReport.put(currencyPair, aggregatedTransaction);
    }
}
