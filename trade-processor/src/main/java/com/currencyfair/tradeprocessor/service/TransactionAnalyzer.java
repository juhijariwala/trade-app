package com.currencyfair.tradeprocessor.service;

import com.currencyfair.tradeprocessor.model.TradeMessage;
import com.currencyfair.tradeprocessor.model.TransactionReport;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Currency;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@Service
public class TransactionAnalyzer {
    private Map<Locale, TransactionReport> totalTransactions = new HashMap<>();
    private ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    private SimpMessagingTemplate template;

    public void ananlyzeAndPush(String message) {

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

        BigDecimal purchaseOrSaleAmount = getPurchaseOrSaleAmount(tradeMessage, originatingCountryCurrency);

        TransactionReport transactionReport;

        if (totalTransactions.containsKey(originatingCountry)) {
            transactionReport = totalTransactions.get(originatingCountry);
            transactionReport.setMaxExchangeRate(tradeMessage.getRate());
            transactionReport.setMinExchangeRate(tradeMessage.getRate());
            transactionReport.add(purchaseOrSaleAmount);

        } else {
            transactionReport = new TransactionReport(originatingCountry,
                originatingCountryCurrency,
                purchaseOrSaleAmount,
                tradeMessage.getRate(),
                tradeMessage.getRate());
        }

        totalTransactions.put(originatingCountry, transactionReport);
    }

    private BigDecimal getPurchaseOrSaleAmount(TradeMessage tradeMessage, Currency originatingCountryCurrency) {
        return tradeMessage.getCurrencyFrom().getCurrencyCode().equals(originatingCountryCurrency.getCurrencyCode()) ?
            tradeMessage.getAmountBuy() :
            tradeMessage.getAmountSell();
    }
}
