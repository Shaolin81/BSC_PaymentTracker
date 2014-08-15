package com.bsc.paymenttracker.task;

import com.bsc.paymenttracker.io.console.ConsoleReader;
import com.bsc.paymenttracker.main.PaymentProcessor;
import com.bsc.paymenttracker.entity.Payment;

import java.util.Scanner;

/**
 * InputReader checks if user entered some value and process it. Needs to be periodically externally called.
 *
 * @author Milos Klvan
 */
public class InputReader implements Runnable {
    public static final String EXIT_COMMAND = "exit";
    public static final String MSG_HELP = "Input payment. For example: \"USD 123.456\". End programm with \"exit\" command.";
    public static final String MSG_ERROR_WHILE_READING_INPUT = "Error while reading inputted line.";
    private PaymentProcessor paymentProcessor;

    public InputReader(PaymentProcessor paymentProcessor) {
        this.paymentProcessor = paymentProcessor;
    }

    @Override
    public void run() {
        String line = null;

        try {
            line = ConsoleReader.getInstance().readLine();
        } catch (Exception exception) {
            System.out.println(MSG_ERROR_WHILE_READING_INPUT);
        }

        if (line != null && !line.isEmpty()) {
            if (!paymentProcessor.isInputLineValid(line)) {
                checkExitCommand(line);

                System.out.println(MSG_HELP);
                return;
            }

            Scanner sc = new Scanner(line);
            paymentProcessor.process(new Payment(sc.next(), sc.nextBigDecimal()));
        }

    }

    private void checkExitCommand(String line) {
        if (EXIT_COMMAND.equals(line)) {
            System.exit(0);
        }
    }

}
