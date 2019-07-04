package com.currencyfair.tradeProcessor.service;

import com.currencyfair.tradeprocessor.model.AggregatedTransaction;
import com.currencyfair.tradeprocessor.service.TransactionAnalyzer;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(SpringRunner.class)
public class TransactionAnalyzerTest {

    @Mock
    private SimpMessagingTemplate simpMessagingTemplate;

    @InjectMocks
    private TransactionAnalyzer transactionAnalyzer;

    @Test
    public void shouldGenerateTransactionReportForCountry() throws IOException {

        String tradeMessage1 = "{\"userId\":\"134256\",\"currencyFrom\":\"EUR\",\"currencyTo\":\"GBP\",\"amountSell\":1000,\"amountBuy\":747.10,\"rate\":0.7471,\"timePlaced\":\"24-JAN-18 10:27:44\",\"originatingCountry\":\"FR\"}";
        String tradeMessage2 = "{\"userId\":\"134256\",\"currencyFrom\":\"EUR\",\"currencyTo\":\"GBP\",\"amountSell\":1494.2,\"amountBuy\":2000,\"rate\":0.4712,\"timePlaced\":\"24-JAN-18 10:27:44\",\"originatingCountry\":\"FR\"}";
        String tradeMessage3 = "{\"userId\":\"134256\",\"currencyFrom\":\"EUR\",\"currencyTo\":\"INR\",\"amountSell\":500,\"amountBuy\":373.55,\"rate\":0.7471,\"timePlaced\":\"24-JAN-18 10:27:44\",\"originatingCountry\":\"IN\"}";

        transactionAnalyzer.generateAndPush(tradeMessage1);
        transactionAnalyzer.generateAndPush(tradeMessage2);
        transactionAnalyzer.generateAndPush(tradeMessage3);

        ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);
        verify(simpMessagingTemplate, times(3)).convertAndSend(eq("/topic/trades"), argumentCaptor.capture());
        assertThat(argumentCaptor.getAllValues().size(), is(3));
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, AggregatedTransaction> expectedMessage = objectMapper.readValue(argumentCaptor.getAllValues().get(2), new TypeReference<Map<String, AggregatedTransaction>>() {
        });
        List<AggregatedTransaction> aggregatedTransactions = new ArrayList<>();
expectedMessage.values().stream().forEach(transaction -> aggregatedTransactions.add(transaction));

        assertThat(aggregatedTransactions.get(0).getCurrencyPair(), is("EUR/INR"));
        assertThat(aggregatedTransactions.get(0).getTotalAmountSent(), is(BigDecimal.valueOf(500)));
        assertThat(aggregatedTransactions.get(0).getTotalAmountReceived(), is(BigDecimal.valueOf(373.55)));
        assertThat(aggregatedTransactions.get(0).getMinExchangeRate(), is(BigDecimal.valueOf(0.7471)));
        assertThat(aggregatedTransactions.get(0).getMaxExchangeRate(), is(BigDecimal.valueOf(0.7471)));
        assertThat(aggregatedTransactions.get(0).getTotalTransactions(), is(BigInteger.valueOf(1)));

        assertThat(aggregatedTransactions.get(1).getCurrencyPair(), is("EUR/GBP"));
        assertThat(aggregatedTransactions.get(1).getTotalAmountSent(), is(BigDecimal.valueOf(2494.2)));
        assertThat(aggregatedTransactions.get(1).getTotalAmountReceived().doubleValue(), is(BigDecimal.valueOf(2747.10).doubleValue()));
        assertThat(aggregatedTransactions.get(1).getMinExchangeRate(), is(BigDecimal.valueOf(0.4712)));
        assertThat(aggregatedTransactions.get(1).getMaxExchangeRate(), is(BigDecimal.valueOf(0.7471)));
        assertThat(aggregatedTransactions.get(1).getTotalTransactions(), is(BigInteger.valueOf(2)));
    }
}
