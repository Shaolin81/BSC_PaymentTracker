package com.bsc.paymenttracker.io.console;

import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.Callable;

/**
 * Task checking in a loop if user entered some input.
 *
 * @author Milos Klvan
 */
public class ConsoleReaderTask implements Callable<String> {

    @Override
    public String call() throws IOException {
        String input = null;
        do {
            Scanner scanInput = new Scanner(System.in);

            if (scanInput.hasNextLine()) {
                input = scanInput.nextLine();
            }
        } while (input == null);
        return input;
    }
}
