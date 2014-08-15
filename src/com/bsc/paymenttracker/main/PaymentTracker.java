package com.bsc.paymenttracker.main;

import com.bsc.paymenttracker.io.file.FileReader;
import com.bsc.paymenttracker.task.CurrencyStatusPrinter;
import com.bsc.paymenttracker.task.InputReader;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * This is the main class of the program. The program can be run with optional parameter on command line containing
 * relative or absolute path to a file with payments to load at startup. Then schedule regular tasks to read
 * user input and to output a list of saldo of all currencies.
 *
 * @author Milos Klvan
 *
 * */
public class PaymentTracker {
    private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(2);

    private static final long INITIAL_DELAY = 0L;
    private static final long CURRENCY_PRINTER_PERIOD_IN_SECONDS = 60;
    private static final long INPUT_READER_PERIOD_IN_MILLISECONDS = 100;

    private PaymentProcessor paymentProcessor = new PaymentProcessor();

    public static void main(String[] args) {
        // check if Console object is asociated with current JVM
        if (System.console() == null) {
            System.exit(-1);
        }

        PaymentTracker paymentTracker = new PaymentTracker();
        paymentTracker.processArgs(args);
        paymentTracker.scheduleTasks();
    }

    private void scheduleTasks() {
        // TODO MKL: it would be appropriate to shutdown service; now program exits with System.exit
        scheduler.scheduleAtFixedRate(new CurrencyStatusPrinter(paymentProcessor),
                INITIAL_DELAY, CURRENCY_PRINTER_PERIOD_IN_SECONDS, TimeUnit.SECONDS);
        scheduler.scheduleAtFixedRate(new InputReader(paymentProcessor),
                INITIAL_DELAY, INPUT_READER_PERIOD_IN_MILLISECONDS, TimeUnit.MILLISECONDS);
    }

    private void loadFile(String filePath) {
        (new FileReader(paymentProcessor)).readFile(filePath);
    }

    private void processArgs(String[] args) {
        if (args.length > 0) {
            if (args.length > 1) {
                System.out.println("Just only one argument with path to the input file can be entered.");
                System.exit(-1);
            }
            String filePath = args[0];
            loadFile(filePath);
        }
    }

    private boolean isConsoleAccessible() {
        return System.console() != null;
    }
}
