package com.currencyfair.tradeprocessor.model;

import java.io.Serializable;
import java.util.Currency;
import java.util.Locale;

public class TotalTransaction implements Serializable {
    private Locale country;
    private Currency currency;
    private Double total;

    public TotalTransaction(Locale country, Currency currency, Double total) {
        this.country = country;
        this.currency = currency;
        this.total = total;
    }

    public TotalTransaction() {
    }

    public void add(Double total) {
        this.total += total;
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

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }
}
