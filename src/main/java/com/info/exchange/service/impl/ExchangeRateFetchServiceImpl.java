package com.info.exchange.service.impl;


import com.info.exchange.constant.AppConstants;
import com.info.exchange.service.ExchangeRateFetchService;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.query.TypedParameterValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Service
public class ExchangeRateFetchServiceImpl implements ExchangeRateFetchService {
    private final Logger logger = LoggerFactory.getLogger(ExchangeRateFetchServiceImpl.class.getName());

    private final RestTemplate restTemplate;

    @Value("${exchange-rate-api.base-url}")
    private String exchangeRateApiBaseUrl;

    @Value("${exchange-rate-api.app-id}")
    private String exchangeRateAppId;

    public ExchangeRateFetchServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public Map<String, Double> fetchExchangeRates(@NotBlank String baseCurrency) {
        try {
            String apiUrl = String.format("%s/latest.json?app_id=%s&base=%s", exchangeRateApiBaseUrl, exchangeRateAppId, baseCurrency);
            logger.info("Fetching exchange rates from API: {}", apiUrl);
            ResponseEntity<Map<String, Object>> responseEntity = restTemplate.exchange(apiUrl, HttpMethod.GET, null, new ParameterizedTypeReference<Map<String, Object>>() {});
            if (responseEntity.getStatusCode().is2xxSuccessful() && responseEntity.hasBody() && Objects.nonNull(responseEntity.getBody())) {
                Map<String, Object> body = responseEntity.getBody();
                return body != null ? (Map<String, Double>) body.get("rates") : new HashMap<>();
            } else {
                throw new RuntimeException(AppConstants.ERROR_FETCHING_CURRENCY_RATE_FROM_API);
            }
        } catch (RestClientException ex) {
            throw new RuntimeException(AppConstants.ERROR_FETCHING_CURRENCY_RATE_FROM_API, ex);
        }
    }


}
