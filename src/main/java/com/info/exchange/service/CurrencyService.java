package com.info.exchange.service;

import com.info.exchange.entity.Currency;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

public interface CurrencyService {

    Currency findByCode(@NotBlank String code);

    List<String> getAllCurrencyCodes();

    Currency addCurrency(@NotBlank String code);


}
