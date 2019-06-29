package com.currencyfair.controller;

import org.junit.Before;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class TradeControllerTest {

    private MockMvc mockMvc;
    private TradeController tradeController;

    @Before
    public void setUp() {
        tradeController = new TradeController();
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
