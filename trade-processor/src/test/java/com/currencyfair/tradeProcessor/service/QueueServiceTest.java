package com.currencyfair.tradeProcessor.service;

import com.currencyfair.tradeprocessor.service.QueueService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.test.context.junit4.SpringRunner;


@RunWith(SpringRunner.class)
public class QueueServiceTest {

    @Mock
    private SimpMessagingTemplate template;

    @InjectMocks
    private QueueService queueService;

    @Test
    public void shouldCalculateTotalTransactionAmountBasedOnPurchaseOrSaleTradeType() {
        String tradeMessage1 = "{\"userId\":\"134256\",\"currencyFrom\":\"EUR\",\"currencyTo\":\"GBP\",\"amountSell\":1000,\"amountBuy\":747.10,\"rate\":0.7471,\"timePlaced\":\"24-JAN-18 10:27:44\",\"originatingCountry\":\"FR\"}";
        String tradeMessage2 = "{\"userId\":\"134256\",\"currencyFrom\":\"GBP\",\"currencyTo\":\"EUR\",\"amountSell\":1494.2,\"amountBuy\":2000,\"rate\":0.7471,\"timePlaced\":\"24-JAN-18 10:27:44\",\"originatingCountry\":\"FR\"}";
        String tradeMessage3 = "{\"userId\":\"134256\",\"currencyFrom\":\"EUR\",\"currencyTo\":\"GBP\",\"amountSell\":500,\"amountBuy\":373.55,\"rate\":0.7471,\"timePlaced\":\"24-JAN-18 10:27:44\",\"originatingCountry\":\"FR\"}";
        String tradeMessage4 = "{\"userId\":\"134256\",\"currencyFrom\":\"EUR\",\"currencyTo\":\"GBP\",\"amountSell\":500,\"amountBuy\":373.55,\"rate\":0.7471,\"timePlaced\":\"24-JAN-18 10:27:44\",\"originatingCountry\":\"GB\"}";
        queueService.handle(tradeMessage1);
        Mockito.verify(template).convertAndSend("/topic/trades", "{\"en_FR\":{\"country\":\"en_FR\",\"currency\":\"EUR\",\"total\":747.1}}");
        queueService.handle(tradeMessage2);
        Mockito.verify(template).convertAndSend("/topic/trades", "{\"en_FR\":{\"country\":\"en_FR\",\"currency\":\"EUR\",\"total\":2241.3}}");
        queueService.handle(tradeMessage3);
        Mockito.verify(template).convertAndSend("/topic/trades", "{\"en_FR\":{\"country\":\"en_FR\",\"currency\":\"EUR\",\"total\":2614.8500000000004}}");
        queueService.handle(tradeMessage4);
        Mockito.verify(template).convertAndSend("/topic/trades", "{\"en_FR\":{\"country\":\"en_FR\",\"currency\":\"EUR\",\"total\":2614.8500000000004},\"en_GB\":{\"country\":\"en_GB\",\"currency\":\"GBP\",\"total\":500.0}}");
    }
}
