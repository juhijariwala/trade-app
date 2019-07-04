package com.currencyfair.tradeprocessor.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.Locale;

public class AggregatedTransaction implements Serializable {
    private Locale originatingCountry;
    private String currencyPair;
    private Date firstTransactionAt;
    private Date lastTransactionAt;
    private BigDecimal totalAmountSent;
    private BigDecimal totalAmountReceived;
    private BigInteger totalTransactions;
    private BigDecimal minExchangeRate;
    private BigDecimal maxExchangeRate;

    public AggregatedTransaction(Locale originatingCountry, String currencyPair, Date firstTransactionAt,
                                 Date lastTransactionAt, BigDecimal totalAmountSent, BigDecimal totalAmountReceived, BigInteger totalTransactions,
                                 BigDecimal minExchangeRate, BigDecimal maxExchangeRate) {
        this.originatingCountry = originatingCountry;
        this.currencyPair = currencyPair;
        this.firstTransactionAt = firstTransactionAt;
        this.lastTransactionAt = lastTransactionAt;
        this.totalAmountSent = totalAmountSent;
        this.totalAmountReceived = totalAmountReceived;
        this.totalTransactions = totalTransactions;
        this.minExchangeRate = minExchangeRate;
        this.maxExchangeRate = maxExchangeRate;
    }

    public AggregatedTransaction() {
    }

    public void add(BigDecimal sent, BigDecimal received) {
        this.totalTransactions = this.totalTransactions.add(BigInteger.ONE);
        this.totalAmountSent = this.totalAmountSent.add(sent);
        this.totalAmountReceived = this.totalAmountReceived.add(received);
    }

    public Locale getOriginatingCountry() {
        return originatingCountry;
    }

    public void setOriginatingCountry(Locale originatingCountry) {
        this.originatingCountry = originatingCountry;
    }

    public BigDecimal getTotalAmountSent() {
        return this.totalAmountSent;
    }

    public void setTotalAmountSent(BigDecimal totalAmountSent) {
        this.totalAmountSent = totalAmountSent;
    }

    public BigDecimal getMinExchangeRate() {
        return minExchangeRate;
    }

    public BigDecimal getMaxExchangeRate() {
        return maxExchangeRate;
    }

    public void setMinRate(BigDecimal exchangeRate) {
        if (this.minExchangeRate.compareTo(exchangeRate) == 1) {
            this.minExchangeRate = exchangeRate;
        }
    }

    public void setMaxRate(BigDecimal exchangeRate) {
        if (this.maxExchangeRate.compareTo(exchangeRate) == -1) {
            this.maxExchangeRate = exchangeRate;
        }
    }

    public String getCurrencyPair() {
        return currencyPair;
    }

    public void setCurrencyPair(String currencyPair) {
        this.currencyPair = currencyPair;
    }

    public Date getFirstTransactionAt() {
        return firstTransactionAt;
    }

    public void setFirstTransactionAt(Date firstTransactionAt) {
        this.firstTransactionAt = firstTransactionAt;
    }

    public Date getLastTransactionAt() {
        return lastTransactionAt;
    }

    public void setLastTransactionAt(Date lastTransactionAt) {
        this.lastTransactionAt = lastTransactionAt;
    }

    public BigInteger getTotalTransactions() {
        return this.totalTransactions;
    }

    public void setTotalTransactions(BigInteger totalTransactions) {
        this.totalTransactions = totalTransactions;
    }


    public void setMinExchangeRate(BigDecimal minExchangeRate) {
        this.minExchangeRate = minExchangeRate;
    }

    public void setMaxExchangeRate(BigDecimal maxExchangeRate) {
        this.maxExchangeRate = maxExchangeRate;
    }

    public BigDecimal getTotalAmountReceived() {
        return this.totalAmountReceived;
    }

    public void setTotalAmountReceived(BigDecimal totalAmountReceived) {
        this.totalAmountReceived = totalAmountReceived;
    }
}
