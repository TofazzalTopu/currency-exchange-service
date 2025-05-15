package com.info.exchange.controller;


import com.info.exchange.constant.AppConstants;
import com.info.exchange.dto.CurrencyRequest;
import com.info.exchange.entity.Currency;
import com.info.exchange.service.CurrencyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@Validated
@CrossOrigin
@RestController
@RequestMapping(AppConstants.CURRENCY_API_URL)
@Tag(name = "Currency Controller", description = "APIs for handling Currency operations")
public class CurrencyController {

    private final CurrencyService service;

    public CurrencyController(CurrencyService service) {
        this.service = service;
    }

    @GetMapping
    @Operation(description = "Fetch Currency list.")
    public ResponseEntity<List<String>> listCurrencies() {
        return ResponseEntity.ok().body(service.getAllCurrencyCodes());
    }

    @PostMapping
    @Operation(description = "Save Currency.")
    public ResponseEntity<Currency> addCurrency(@Valid @RequestBody CurrencyRequest currencyRequest) throws URISyntaxException {
        Currency currency = service.addCurrency(currencyRequest.getCurrencyCode().trim().toUpperCase());
        return ResponseEntity.created(new URI(AppConstants.CURRENCY_API_URL)).body(currency);
    }

}
