package com.info.exchange.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.info.exchange.constant.AppConstants;
import com.info.exchange.dto.CurrencyRequest;
import com.info.exchange.entity.Currency;
import com.info.exchange.service.CurrencyService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CurrencyController.class)
class CurrencyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CurrencyService currencyService;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
    }

    @Test
    void testListCurrencies() throws Exception {
        when(currencyService.getAllCurrencyCodes()).thenReturn(List.of("USD", "EUR"));

        mockMvc.perform(get(AppConstants.CURRENCY_API_URL))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0]", is("USD")))
                .andExpect(jsonPath("$[1]", is("EUR")));
    }

    @Test
    void testAddCurrency() throws Exception {
        CurrencyRequest request = new CurrencyRequest();
        request.setCurrencyCode("usd");

        Currency savedCurrency = new Currency();
        savedCurrency.setCode("USD");

        when(currencyService.addCurrency(anyString())).thenReturn(savedCurrency);

        mockMvc.perform(post(AppConstants.CURRENCY_API_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.code").value("USD"));
    }

    @Test
    void testAddCurrency_AlreadyExists() throws Exception {
        CurrencyRequest request = new CurrencyRequest();
        request.setCurrencyCode("usd");

        when(currencyService.addCurrency(anyString())).thenThrow(new RuntimeException("Currency already exists with code: USD"));

        mockMvc.perform(post(AppConstants.CURRENCY_API_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(result -> result.getResolvedException().getMessage().contains("Currency already exists"));
    }
}
