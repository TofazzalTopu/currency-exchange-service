package com.info.exchange.service;

import com.info.exchange.repository.ExchangeRateRepository;
import com.info.exchange.service.impl.ExchangeRateServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ExchangeRateServiceTest {

    @Mock
    private CurrencyService currencyService;

    @Mock
    private ExchangeRateRepository exchangeRateRepository;

    @Mock
    private ExchangeRateFetchService exchangeRateFetchService;

    @InjectMocks
    private ExchangeRateServiceImpl exchangeRateService;

    @Test
    void testFetchExchangeRates_shouldUpdateInMemoryMap() {
        String baseCurrency = "USD";
        when(currencyService.getAllCurrencyCodes()).thenReturn(List.of("EUR"));
        when(exchangeRateFetchService.fetchExchangeRates(baseCurrency))
                .thenReturn(Map.of("EUR", 0.85));

        exchangeRateService.fetchExchangeRates(baseCurrency);

        assertEquals(BigDecimal.valueOf(0.85), exchangeRateService.getRate("EUR"));
    }

    @Test
    void testGetRate_whenNotPresent_thenThrowException() {
        assertThrows(RuntimeException.class, () -> exchangeRateService.getRate("JPY"));
    }
}

