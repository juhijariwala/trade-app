package com.currencyfair.tradepublisher.controller;

import com.currencyfair.tradepublisher.service.SqsService;
import com.currencyfair.tradepublisher.service.TradePublisherService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
public class TradeControllerTest {

    private MockMvc mockMvc;

    @Mock
    private TradePublisherService service;
    @InjectMocks
    private TradeController tradeController;

    @Before
    public void setUp() {

        mockMvc = MockMvcBuilders.standaloneSetup(tradeController).build();
    }

    @Test
    public void shouldPublishTradeMessage() throws Exception {
        String requestBody = "{\"userId\": \"134256\", \"currencyFrom\": \"EUR\", \"currencyTo\": \"GBP\",\n" +
            "\"amountSell\": 1000, \"amountBuy\": 747.10, \"rate\": 0.7471,\n" +
            "\"timePlaced\" : \"24-JAN-18 10:27:44\", \"originatingCountry\" : \"FR\"}";

        mockMvc.perform(post("/trade-publisher/trade")
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestBody))
            .andExpect(status().isOk());
    }

}
