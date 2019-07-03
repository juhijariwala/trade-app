package com.currencyfair.tradepublisher.dto;

import com.currencyfair.tradepublisher.validation.ValidCurrencyCode;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

public class TradeMessage {
    private String userId;
    private String currencyFrom;
    private String currencyTo;
    private Double amountSell;
    private Double amountBuy;
    private Double rate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "DD-MMMM-yy")
    private Date timePlaced;
    @ValidCurrencyCode
    private String originatingCountry;

    public String getUserId() {
        return userId;
    }

    public String getCurrencyFrom() {
        return currencyFrom;
    }

    public String getCurrencyTo() {
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

    public String getOriginatingCountry() {
        return originatingCountry;
    }

}
