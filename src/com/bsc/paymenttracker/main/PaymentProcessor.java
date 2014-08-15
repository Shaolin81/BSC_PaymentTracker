package com.bsc.paymenttracker.main;

import com.bsc.paymenttracker.entity.Payment;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * PaymentProcessor handles inputted Payments.
 *
 * @author Milos Klvan
 */
public class PaymentProcessor {
    public static final String PAYMENT_INPUT_VALIDATOR_REGEXP = "^[A-Z]{3}[ ]-?\\d{1,12}([.]\\d{1,3})?$";
    public static final String MSG_PAYMENT_NOT_APPLIED = "Payment was not applied due to unexpected error.";
    public static final String MSG_FATAL_ERROR = "Fatal error. Program will exit.";

    public static List<Payment> paymentsHistory = new ArrayList<>(100);
    public static Map<String, BigDecimal> currencies = new ConcurrentHashMap<String, BigDecimal>();

    /**
     * Consistenly applies payment.
     * @param payment payment to apply
     */
    public void process(Payment payment) {
        if (payment == null) {
            return;
        }

        synchronized (this) {
            applyPaymentConsistently(payment);
        }
    }

    private void applyPaymentConsistently(Payment payment) {
        paymentsHistory.add(payment);
        try {
            applyPayment(payment);
        } catch (Exception e1) {
            System.out.println(MSG_PAYMENT_NOT_APPLIED);
            try {
                paymentsHistory.remove(paymentsHistory.size()-1);
            } catch (Exception e2) {
                System.out.println(MSG_FATAL_ERROR);
                System.exit(-1);
            }
        }
    }

    private void applyPayment(Payment payment) {
        BigDecimal origValue = currencies.get(payment.getCurrencyName());
        BigDecimal paymentValue = payment.getAmount();
        if (origValue == null) {
            currencies.put(payment.getCurrencyName(), paymentValue);
        } else {
            currencies.put(payment.getCurrencyName(), origValue.add(paymentValue));
        }
    }

    public boolean isInputLineValid(String line) {
        return line.matches(PAYMENT_INPUT_VALIDATOR_REGEXP);
    }

    /**
     * Returns unordered list of currencies in format "currencyName saldo". Currencies with 0 saldo are not included.
     * @return list of currencies
     */
    public List<String> getStateOfCurrencies() {
        List<String> list = new ArrayList<String>();
        for (Map.Entry<String, BigDecimal> entry : currencies.entrySet()) {
            if (entry.getValue().compareTo(BigDecimal.ZERO) != 0) {
                list.add(entry.getKey() + " " + entry.getValue());
            }
        }

        return list;
    }
}
