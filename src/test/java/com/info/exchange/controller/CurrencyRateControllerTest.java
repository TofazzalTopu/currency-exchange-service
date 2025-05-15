package com.info.exchange.controller;


import com.info.exchange.constant.AppConstants;
import com.info.exchange.service.ExchangeRateService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CurrencyRateController.class)
public class CurrencyRateControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ExchangeRateService exchangeRateService;

    @Test
    void testGetRate_ValidCurrency() throws Exception {
        when(exchangeRateService.getRate("USD")).thenReturn(BigDecimal.valueOf(108.50));

        mockMvc.perform(get(AppConstants.CURRENCY_API_URL + "/rates/USD")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(108.50));
    }

    @Test
    void testGetRate_InvalidCurrency() throws Exception {
        when(exchangeRateService.getRate("XYZ")).thenReturn(null);

        mockMvc.perform(get(AppConstants.CURRENCY_API_URL + "/rates/XYZ")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}
