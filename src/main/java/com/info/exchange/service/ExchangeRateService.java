package com.info.exchange.service;

import com.info.exchange.entity.ExchangeRate;
import jakarta.validation.constraints.NotBlank;

import java.math.BigDecimal;
import java.util.Optional;

public interface ExchangeRateService {

    Optional<ExchangeRate> findByCurrencyCode(@NotBlank String currencyCode);
    ExchangeRate saveExchangeRate(ExchangeRate exchangeRate);

    BigDecimal getRate(@NotBlank String currencyCode);

    void fetchExchangeRates(@NotBlank String baseCurrencyCode);
}
