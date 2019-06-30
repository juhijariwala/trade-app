package com.currencyfair.tradepublisher.dto;

import com.currencyfair.tradepublisher.constants.Country;
import com.currencyfair.tradepublisher.constants.Currency;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

public class TradeMessage {
    private String userId;
    private Currency currencyFrom;
    private Currency currencyTo;
    private Double amountSell;
    private Double amountBuy;
    private Double rate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "DD-MMMM-yy")
    private Date timePlaced;
    private Country originatingCountry;

    public String getUserId() {
        return userId;
    }

    public Currency getCurrencyFrom() {
        return currencyFrom;
    }

    public Currency getCurrencyTo() {
        return currencyTo;
    }

    public Double getAmountSell() {
        return amountSell;
    }

    public Double getAmountBuy() {
        return amountBuy;
    }

    public Double getRate() {
        return rate;
    }

    public Date getTimePlaced() {
        return timePlaced;
    }

    public Country getOriginatingCountry() {
        return originatingCountry;
    }

}
