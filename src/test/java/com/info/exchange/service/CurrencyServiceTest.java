package com.info.exchange.service;

import com.info.exchange.entity.Currency;
import com.info.exchange.repository.CurrencyRepository;
import com.info.exchange.service.impl.CurrencyServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CurrencyServiceTest {

    @Mock
    private CurrencyRepository currencyRepository;

    @InjectMocks
    private CurrencyServiceImpl currencyService;

    @Test
    void testAddCurrency_whenNewCurrency_thenSavesSuccessfully() {
        String code = "EUR";
        when(currencyRepository.findByCode(code)).thenReturn(Optional.empty());
        when(currencyRepository.save(any(Currency.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Currency result = currencyService.addCurrency(code);

        assertEquals("EUR", result.getCode());

        verify(currencyRepository, times(1)).save(any(Currency.class));
    }

    @Test
    void testAddCurrency_whenDuplicate_thenThrowsException() {
        String code = "USD";
        Currency currency = new Currency(1L, code);
        when(currencyRepository.findByCode(code)).thenReturn(Optional.of(currency));

        assertThrows(RuntimeException.class, () -> currencyService.addCurrency(code));
    }

    @Test
    void testGetAllCurrencyCodes() {
        Currency currency1 = new Currency(1L, "USD");
        Currency currency2 = new Currency(2L, "EUR");
        List<Currency> currencies = List.of(currency1, currency2);

        when(currencyRepository.findAll()).thenReturn(currencies);
        List<String> codes = currencyService.getAllCurrencyCodes();
        assertEquals(List.of("USD", "EUR"), codes);
    }
}

