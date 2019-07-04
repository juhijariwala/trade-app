package com.currencyfair.tradepublisher.service;

import com.currencyfair.tradepublisher.dto.TradeMessage;
import com.currencyfair.tradepublisher.validation.ValidationException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.io.IOException;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.*;


public class TradePublisherServiceTest {

    private static final String TEST_QUEUE = "TestQueue";
    @Mock
    private SqsService sqsService;

    private TradePublisherService tradePublisherService;

    @Before
    public void setUp() {
        sqsService = mock(SqsService.class);
        tradePublisherService = new TradePublisherService(sqsService, TEST_QUEUE);
    }

    @Test
    public void shouldSendValidMessageToQueue() throws IOException {
        String messgae = "{\"userId\":\"134256\",\"currencyFrom\":\"EUR\",\"currencyTo\":\"GBP\",\"amountSell\":1000, \"amountBuy\": 747.10,\"rate\":0.7471,\"timePlaced\":\"24-JAN-1810:27:44\",\"originatingCountry\":\"FR\"}";

        ObjectMapper objectMapper = new ObjectMapper();
        TradeMessage tradeMessage = objectMapper.readValue(messgae, TradeMessage.class);
        String expectedMessage = objectMapper.writeValueAsString(tradeMessage);

        tradePublisherService.publishMessage(tradeMessage);

        verify(sqsService).sendMessage(expectedMessage, TEST_QUEUE);
    }

    @Test
    public void shouldThrowExceptionIfInvalidCountryIsPassed() throws IOException {
        String messgae = "{\"userId\":\"134256\",\"currencyFrom\":\"EUR\",\"currencyTo\":\"GBP\",\"amountSell\":1000, \"amountBuy\": 747.10,\"rate\":0.7471,\"timePlaced\":\"24-JAN-1810:27:44\",\"originatingCountry\":\"INVALID_COUNTRY\"}";
        ObjectMapper objectMapper = new ObjectMapper();
        TradeMessage tradeMessage = objectMapper.readValue(messgae, TradeMessage.class);

        try{
            tradePublisherService.publishMessage(tradeMessage);
            fail();
        }catch (ValidationException e){
            assertThat(e.getMsg(), is("Invalid Country code"));
        }

    }
    @Test
    public void shouldThrowExceptionIfInvalidCurrencyFROMIsPassed() throws IOException {
        String messgae = "{\"userId\":\"134256\",\"currencyFrom\":\"INVALID_CURRENCY\",\"currencyTo\":\"GBP\",\"amountSell\":1000, \"amountBuy\": 747.10,\"rate\":0.7471,\"timePlaced\":\"24-JAN-1810:27:44\",\"originatingCountry\":\"FR\"}";
        ObjectMapper objectMapper = new ObjectMapper();
        TradeMessage tradeMessage = objectMapper.readValue(messgae, TradeMessage.class);

        try{
            tradePublisherService.publishMessage(tradeMessage);
            fail();
        }catch (ValidationException e){
            assertThat(e.getMsg(), is("Invalid Currency code"));
        }
        verify(sqsService,never()).sendMessage(anyString(), anyString());
    }

    @Test
    public void shouldThrowExceptionIfInvalidCurrencyTOIsPassed() throws IOException {
        String messgae = "{\"userId\":\"134256\",\"currencyFrom\":\"ERT\",\"currencyTo\":\"INVALID_CURRENCY\",\"amountSell\":1000, \"amountBuy\": 747.10,\"rate\":0.7471,\"timePlaced\":\"24-JAN-1810:27:44\",\"originatingCountry\":\"FR\"}";
        ObjectMapper objectMapper = new ObjectMapper();
        TradeMessage tradeMessage = objectMapper.readValue(messgae, TradeMessage.class);

        try{
            tradePublisherService.publishMessage(tradeMessage);
            fail();
        }catch (ValidationException e){
            assertThat(e.getMsg(), is("Invalid Currency code"));
        }

        verify(sqsService,never()).sendMessage(anyString(), anyString());
    }

    @Test
    public void shouldThrowExceptionIfInvalidSellOrBuyAmount() throws IOException {
        String messgae = "{\"userId\":\"134256\",\"currencyFrom\":\"EUR\",\"currencyTo\":\"INR\",\"amountSell\":1000, \"amountBuy\": 1000.10,\"rate\":0.7471,\"timePlaced\":\"24-JAN-1810:27:44\",\"originatingCountry\":\"FR\"}";
        ObjectMapper objectMapper = new ObjectMapper();
        TradeMessage tradeMessage = objectMapper.readValue(messgae, TradeMessage.class);

        try{
            tradePublisherService.publishMessage(tradeMessage);
            fail();
        }catch (ValidationException e){
            assertThat(e.getMsg(), is("Please enter valid sell or buy rate according to rate "));
        }

        verify(sqsService,never()).sendMessage(anyString(), anyString());
    }
    @Test
    public void shouldThrowExceptionIfInvalidSellOrBuyAmountPassed() throws IOException {
        String messgae = "{\"userId\":\"134256\",\"currencyFrom\":\"USD\",\"currencyTo\":\"INR\",\"amountSell\":747.10, \"amountBuy\": 1000,\"rate\":0.7471,\"timePlaced\":\"24-JAN-1810:27:44\",\"originatingCountry\":\"IN\"}";
        ObjectMapper objectMapper = new ObjectMapper();
        TradeMessage tradeMessage = objectMapper.readValue(messgae, TradeMessage.class);
          tradePublisherService.publishMessage(tradeMessage);
        String actualMessage = objectMapper.writeValueAsString(tradeMessage);
        verify(sqsService).sendMessage(actualMessage, TEST_QUEUE);
    }

}
