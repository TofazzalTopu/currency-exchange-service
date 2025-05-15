package com.info.exchange.service;

import java.util.Map;

public interface ExchangeRateFetchService {
    Map<String, Double> fetchExchangeRates(String baseCurrency);
}
