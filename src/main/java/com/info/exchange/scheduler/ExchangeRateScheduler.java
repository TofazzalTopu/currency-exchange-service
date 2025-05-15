package com.info.exchange.scheduler;


import com.info.exchange.service.ExchangeRateService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
public class ExchangeRateScheduler {

    private final ExchangeRateService exchangeRateService;

    @Value("${exchange-rate-api.base-url}")
    private String exchangeRateApiBaseUrl;

    @Value("${exchange-rate-api.app-id}")
    private String exchangeRateAppId;

    public ExchangeRateScheduler(ExchangeRateService exchangeRateService) {
        this.exchangeRateService = exchangeRateService;
    }

    @Scheduled(cron = "0 0 * * * *")
    public void fetchExchangeRates() {
        exchangeRateService.fetchExchangeRates("USD");
    }

}
