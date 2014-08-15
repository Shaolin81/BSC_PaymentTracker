package com.bsc.paymenttracker.entity;

import java.math.BigDecimal;

/**
 * Simple object wraping currencyName and amount.
 * @author Milos Klvan
 */
public class Payment {
    // TODO MKL: save time
    private final String currencyName;
    private final BigDecimal amount;

    public Payment(String currencyName, BigDecimal amount) {
        this.currencyName = currencyName;
        this.amount = amount;
    }

    public String getCurrencyName() {
        return currencyName;
    }

    public BigDecimal getAmount() {
        return amount;
    }
}
