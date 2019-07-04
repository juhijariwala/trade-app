package com.currencyfair.tradeprocessor.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Date;

public class TradeMessage {
    private String userId;
    private Currency currencyFrom;
    private Currency currencyTo;
    private BigDecimal amountSell;
    private BigDecimal amountBuy;
    private BigDecimal rate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "DD-MMMM-yy")
    private Date timePlaced;
    private String originatingCountry;

    public String getUserId() {
        return userId;
    }

    public Currency getCurrencyFrom() {
        return currencyFrom;
    }

    public Currency getCurrencyTo() {
        return currencyTo;
    }

    public BigDecimal getAmountSell() {
        return amountSell;
    }

    public BigDecimal getAmountBuy() {
        return amountBuy;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public Date getTimePlaced() {
        return timePlaced;
    }

    public String getOriginatingCountry() {
        return originatingCountry;
    }

}
