package com.currencyfair.tradeProcessor.service;

import com.currencyfair.tradeprocessor.service.TransactionAnalyzer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import static org.mockito.Mockito.verify;

@RunWith(SpringRunner.class)
public class TransactionAnalyzerTest {

    @Mock
    private SimpMessagingTemplate simpMessagingTemplate;

    @InjectMocks
    private TransactionAnalyzer transactionAnalyzer;

    @Test
    public void shouldGenerateTransactionReportForCountry() {

        String tradeMessage1 = "{\"userId\":\"134256\",\"currencyFrom\":\"EUR\",\"currencyTo\":\"GBP\",\"amountSell\":1000,\"amountBuy\":747.10,\"rate\":0.7471,\"timePlaced\":\"24-JAN-18 10:27:44\",\"originatingCountry\":\"FR\"}";
        String tradeMessage2 = "{\"userId\":\"134256\",\"currencyFrom\":\"GBP\",\"currencyTo\":\"EUR\",\"amountSell\":1494.2,\"amountBuy\":2000,\"rate\":0.7471,\"timePlaced\":\"24-JAN-18 10:27:44\",\"originatingCountry\":\"FR\"}";
        String tradeMessage3 = "{\"userId\":\"134256\",\"currencyFrom\":\"EUR\",\"currencyTo\":\"INR\",\"amountSell\":500,\"amountBuy\":373.55,\"rate\":0.7471,\"timePlaced\":\"24-JAN-18 10:27:44\",\"originatingCountry\":\"IN\"}";

        transactionAnalyzer.ananlyzeAndPush(tradeMessage1);
        transactionAnalyzer.ananlyzeAndPush(tradeMessage2);
        transactionAnalyzer.ananlyzeAndPush(tradeMessage3);
        String expectedReport1 = "{\"en_FR\":{\"country\":\"en_FR\",\"currency\":\"EUR\",\"total\":747.10,\"minExchangeRate\":0.7471,\"maxExchangeRate\":0.7471}}";
        String expectedReport2 = "{\"en_FR\":{\"country\":\"en_FR\",\"currency\":\"EUR\",\"total\":2241.30,\"minExchangeRate\":0.7471,\"maxExchangeRate\":0.7471}}";
        String expectedReport3 = "{\"en_IN\":{\"country\":\"en_IN\",\"currency\":\"INR\",\"total\":500,\"minExchangeRate\":0.7471,\"maxExchangeRate\":0.7471},\"en_FR\":{\"country\":\"en_FR\",\"currency\":\"EUR\",\"total\":2241.30,\"minExchangeRate\":0.7471,\"maxExchangeRate\":0.7471}}";

        verify(simpMessagingTemplate).convertAndSend("/topic/trades", expectedReport1);
        verify(simpMessagingTemplate).convertAndSend("/topic/trades", expectedReport2);
        verify(simpMessagingTemplate).convertAndSend("/topic/trades", expectedReport3);
    }

    @Test
    public void shouldGenerateCalculateMinAndMaxRate() {

        String tradeMessage1 = "{\"userId\":\"134256\",\"currencyFrom\":\"EUR\",\"currencyTo\":\"GBP\",\"amountSell\":1000,\"amountBuy\":747.10,\"rate\":0.7471,\"timePlaced\":\"24-JAN-18 10:27:44\",\"originatingCountry\":\"FR\"}";
        String tradeMessage2 = "{\"userId\":\"134256\",\"currencyFrom\":\"GBP\",\"currencyTo\":\"EUR\",\"amountSell\":1494.2,\"amountBuy\":2000,\"rate\":0.271,\"timePlaced\":\"24-JAN-18 10:27:44\",\"originatingCountry\":\"FR\"}";
        String tradeMessage3 = "{\"userId\":\"134256\",\"currencyFrom\":\"EUR\",\"currencyTo\":\"INR\",\"amountSell\":500,\"amountBuy\":373.55,\"rate\":0.7471,\"timePlaced\":\"24-JAN-18 10:27:44\",\"originatingCountry\":\"IN\"}";
        String tradeMessage4 = "{\"userId\":\"134256\",\"currencyFrom\":\"EUR\",\"currencyTo\":\"INR\",\"amountSell\":500,\"amountBuy\":373.55,\"rate\":0.900,\"timePlaced\":\"24-JAN-18 10:27:44\",\"originatingCountry\":\"IN\"}";

        transactionAnalyzer.ananlyzeAndPush(tradeMessage1);
        transactionAnalyzer.ananlyzeAndPush(tradeMessage2);
        transactionAnalyzer.ananlyzeAndPush(tradeMessage3);
        transactionAnalyzer.ananlyzeAndPush(tradeMessage4);

        String expectedReport1 = "{\"en_FR\":{\"country\":\"en_FR\",\"currency\":\"EUR\",\"total\":747.10,\"minExchangeRate\":0.7471,\"maxExchangeRate\":0.7471}}";
        String expectedReport2 = "{\"en_FR\":{\"country\":\"en_FR\",\"currency\":\"EUR\",\"total\":2241.30,\"minExchangeRate\":0.271,\"maxExchangeRate\":0.7471}}";
        String expectedReport3 = "{\"en_IN\":{\"country\":\"en_IN\",\"currency\":\"INR\",\"total\":500,\"minExchangeRate\":0.7471,\"maxExchangeRate\":0.7471},\"en_FR\":{\"country\":\"en_FR\",\"currency\":\"EUR\",\"total\":2241.30,\"minExchangeRate\":0.271,\"maxExchangeRate\":0.7471}}";
        String expectedReport4 = "{\"en_IN\":{\"country\":\"en_IN\",\"currency\":\"INR\",\"total\":1000,\"minExchangeRate\":0.7471,\"maxExchangeRate\":0.900},\"en_FR\":{\"country\":\"en_FR\",\"currency\":\"EUR\",\"total\":2241.30,\"minExchangeRate\":0.271,\"maxExchangeRate\":0.7471}}";


        verify(simpMessagingTemplate).convertAndSend("/topic/trades", expectedReport1);
        verify(simpMessagingTemplate).convertAndSend("/topic/trades", expectedReport2);
        verify(simpMessagingTemplate).convertAndSend("/topic/trades", expectedReport3);
        verify(simpMessagingTemplate).convertAndSend("/topic/trades", expectedReport4);
    }
}
