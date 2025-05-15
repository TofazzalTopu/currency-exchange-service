package com.info.exchange.service.impl;

import com.info.exchange.entity.Currency;
import com.info.exchange.repository.CurrencyRepository;
import com.info.exchange.service.CurrencyService;
import jakarta.validation.constraints.NotBlank;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CurrencyServiceImpl implements CurrencyService {

    private final CurrencyRepository currencyRepository;

    public CurrencyServiceImpl(CurrencyRepository currencyRepository) {
        this.currencyRepository = currencyRepository;
    }

    @Override
    public Currency findByCode(@NotBlank String code) {
        return currencyRepository.findByCode(code.toUpperCase()).orElseThrow(() -> new RuntimeException("Currency not found with code: " + code));
    }

    @Override
    public List<String> getAllCurrencyCodes() {
        return currencyRepository.findAll().stream().map(Currency::getCode).toList();
    }

    public Currency addCurrency(@NotBlank String code) {
        Optional<Currency> optionalCurrency = currencyRepository.findByCode(code);
        if (optionalCurrency.isPresent()) throw new RuntimeException("Currency already exists with code: " + code);

        Currency currency = new Currency();
        currency.setCode(code.toUpperCase());
        return currencyRepository.save(currency);
    }


}
