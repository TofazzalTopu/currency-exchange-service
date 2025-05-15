package com.info.exchange.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "exchange_rate")
public class ExchangeRate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "currency_code", nullable = false, length = 3, unique = true)
    private String currencyCode;

    @Column(name = "rate", nullable = false)
    private BigDecimal rate;

    public ExchangeRate() {
    }

    public ExchangeRate(Long id, String currencyCode, BigDecimal rate) {
        this.id = id;
        this.currencyCode = currencyCode;
        this.rate = rate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }
}
