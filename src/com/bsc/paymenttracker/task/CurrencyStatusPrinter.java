package com.bsc.paymenttracker.task;

import com.bsc.paymenttracker.main.PaymentProcessor;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

/**
 * CurrencyStatusPrinter prints to the console current saldo of currencies.
 *
 * @author Milos Klvan
 */
public class CurrencyStatusPrinter implements Runnable {
    public static final String MSG_EMPTY_LIST = "**** EMPTY LIST ****";
    public static final String MSG_CURRENCY_SALDO = "**** CURRENCY SALDO ****";
    public static final String MSG_END = "**** END ****";

    private static final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

    PaymentProcessor paymentProcessor;

    public CurrencyStatusPrinter(PaymentProcessor paymentProcessor) {
        this.paymentProcessor = paymentProcessor;
    }

    @Override
    public void run() {
        List<String> list = getSortedCurrencies();

        if (!list.isEmpty()) {
            System.out.println(MSG_CURRENCY_SALDO + " " + getTime());
            for (String s : list) {
                System.console().writer().println(s);
            }
            System.out.println(MSG_END);
        } else {
            System.out.println(MSG_EMPTY_LIST + " " + getTime());
        }
    }

    private List<String> getSortedCurrencies() {
        List<String> list = paymentProcessor.getStateOfCurrencies();
        Collections.sort(list);
        return list;
    }

    private String getTime() {
        return sdf.format(Calendar.getInstance().getTime());
    }
}
