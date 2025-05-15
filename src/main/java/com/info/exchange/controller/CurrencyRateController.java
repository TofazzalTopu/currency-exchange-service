package com.info.exchange.controller;


import com.info.exchange.constant.AppConstants;
import com.info.exchange.service.ExchangeRateService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Optional;

@Validated
@CrossOrigin
@RestController
@RequestMapping(AppConstants.CURRENCY_API_URL)
@Tag(name = "Currency Rate Controller", description = "APIs for handling Currency Rate operations")
public class CurrencyRateController {

    private final ExchangeRateService exchangeRateService;

    public CurrencyRateController(ExchangeRateService exchangeRateService) {
        this.exchangeRateService = exchangeRateService;
    }

    @GetMapping("/rates/{currencyCode}")
    @Operation(description = "Get Currency Rate by currency code.")
    public ResponseEntity<BigDecimal> getRate(@PathVariable String currencyCode) {
        BigDecimal rate = exchangeRateService.getRate(currencyCode.toUpperCase());
        return Optional.ofNullable(rate).map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
