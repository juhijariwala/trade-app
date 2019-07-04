package com.currencyfair.tradeprocessor.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Currency;
import java.util.Locale;

public class TransactionReport implements Serializable {
    private Locale country;
    private Currency currency;
    private BigDecimal total;
    private BigDecimal minExchangeRate;
    private BigDecimal maxExchangeRate;

    public TransactionReport(Locale country, Currency currency, BigDecimal total, BigDecimal minExchangeRate, BigDecimal maxExchangeRate) {
        this.country = country;
        this.currency = currency;
        this.total = total;
        this.minExchangeRate = minExchangeRate;
        this.maxExchangeRate = maxExchangeRate;
    }

    public TransactionReport() {
    }

    public void add(BigDecimal total) {
        this.total = this.total.add(total);
    }

    public Locale getCountry() {
        return country;
    }

    public void setCountry(Locale country) {
        this.country = country;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public BigDecimal getTotal() {
        return this.total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public BigDecimal getMinExchangeRate() {
        return minExchangeRate;
    }

    public BigDecimal getMaxExchangeRate() {
        return maxExchangeRate;
    }

    public void setMinExchangeRate(BigDecimal exchangeRate) {
        if (this.minExchangeRate.compareTo(exchangeRate) == 1) {
            this.minExchangeRate = exchangeRate;
        }
    }

    public void setMaxExchangeRate(BigDecimal exchangeRate) {
        if (this.maxExchangeRate.compareTo(exchangeRate) == -1) {
            this.maxExchangeRate = exchangeRate;
        }
    }
}
