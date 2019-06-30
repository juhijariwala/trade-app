package com.currencyfair.tradepublisher.controller;

import org.junit.Before;
import org.junit.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class HealthControllerTest {
    private MockMvc mockMvc;
    private HealthController healthController;

    @Before
    public void setUp() {
        healthController = new HealthController();
        mockMvc = MockMvcBuilders.standaloneSetup(healthController).build();
    }

    @Test
    public void shouldCheckHealthService() throws Exception {
        mockMvc.perform(get("/health")).andExpect(status().isOk()).andExpect(content().string("Service is up"));
    }
}
