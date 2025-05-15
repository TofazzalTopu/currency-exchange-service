package com.info.exchange.service.impl;

import com.info.exchange.constant.AppConstants;
import com.info.exchange.entity.ExchangeRate;
import com.info.exchange.repository.ExchangeRateRepository;
import com.info.exchange.service.CurrencyService;
import com.info.exchange.service.ExchangeRateFetchService;
import com.info.exchange.service.ExchangeRateService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Transactional
public class ExchangeRateServiceImpl implements ExchangeRateService {

    private final Logger logger = LoggerFactory.getLogger(ExchangeRateServiceImpl.class.getName());
    private final CurrencyService currencyService;
    private final ExchangeRateRepository exchangeRateRepository;
    private final ExchangeRateFetchService exchangeRateFetchService;

    private final Map<String, BigDecimal> exchangeRateMap = new ConcurrentHashMap<>();

    public ExchangeRateServiceImpl(CurrencyService currencyService, ExchangeRateRepository exchangeRateRepository, ExchangeRateFetchService exchangeRateFetchService) {
        this.currencyService = currencyService;
        this.exchangeRateRepository = exchangeRateRepository;
        this.exchangeRateFetchService = exchangeRateFetchService;
    }

    @Override
    public Optional<ExchangeRate> findByCurrencyCode(String currencyCode) {
        return exchangeRateRepository.findByCurrencyCode(currencyCode.toUpperCase());
    }

    @Override
    public ExchangeRate saveExchangeRate(ExchangeRate exchangeRate) {
        Optional<ExchangeRate> currencyCode = findByCurrencyCode(exchangeRate.getCurrencyCode());
        return currencyCode.orElseGet(() -> exchangeRateRepository.save(exchangeRate));
    }

    @Override
    public BigDecimal getRate(String currencyCode) {
        BigDecimal rate = exchangeRateMap.get(currencyCode.toUpperCase());
        if (Objects.isNull(rate)) throw new RuntimeException("Exchange rate not found for currency: " + currencyCode);
        return rate;
    }

    @Override
    public void fetchExchangeRates(String baseCurrencyCode) {
        try {
            List<String> currencies = currencyService.getAllCurrencyCodes();
            if (currencies.isEmpty()) return;
            Map<String, Double> rates = exchangeRateFetchService.fetchExchangeRates(baseCurrencyCode);
            if (rates == null || rates.isEmpty()) {
                throw new RuntimeException(AppConstants.EXCHANGE_RATE_NOT_FOUND_FOR_BASE_CURRENCY + baseCurrencyCode);
            }
            currencies.forEach(currency -> {
                Number number = rates.get(currency.toUpperCase());
                if (number == null) {
                    logger.error("No exchange rate found for currency: {}", currency);
                    throw new RuntimeException(AppConstants.EXCHANGE_RATE_NOT_FOUND + currency);
                }
                Double rate = number.doubleValue();
                updateExchangeRate(currency, BigDecimal.valueOf(rate));
            });
            logger.info(AppConstants.CURRENCY_RATE_FETCH_SUCCESSFULLY, baseCurrencyCode);
        } catch (Exception e) {
            logger.error(AppConstants.ERROR_FETCHING_CURRENCY_RATE, e.getMessage());
        }
    }


    private void updateExchangeRate(String currency, BigDecimal rate) {
        Optional<ExchangeRate> exchangeRate = findByCurrencyCode(currency.toUpperCase());
        exchangeRateMap.put(currency.toUpperCase(), rate);
        if (exchangeRate.isEmpty()) {
            saveExchangeRate(currency, rate);
        }
    }

    private ExchangeRate saveExchangeRate(String currency, BigDecimal rate) {
        ExchangeRate entity = new ExchangeRate();
        entity.setCurrencyCode(currency.toUpperCase());
        entity.setRate(rate);
        return saveExchangeRate(entity);
    }
}
