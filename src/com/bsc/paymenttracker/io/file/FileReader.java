package com.bsc.paymenttracker.io.file;

import com.bsc.paymenttracker.entity.Payment;
import com.bsc.paymenttracker.main.PaymentProcessor;

import java.io.BufferedReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

/**
 * FileReader can read file with payments to load and apply lines with valid payments. Empty lines are skipped. If there
 * is any invalid entry then the loading is interrupted and program will exit.
 *
 * @author Milos Klvan
 */
public class FileReader {
    public static final String MSG_FILE_NOT_FOUND = "Cannot load entered file. Check path or file name. Program will exit.";
    public static final String MSG_ERROR_WHILE_READING = "Error while reading the input file. Program will exit. ";

    PaymentProcessor paymentProcessor;

    public FileReader(PaymentProcessor paymentProcessor) {
        this.paymentProcessor = paymentProcessor;
    }

    /**
     * Reads a file with payments and applies lines with valid payments.
     * @param file relative or absolute path to a file
     */
    public void readFile(String file) {
        // TODO MKL: return boolean value if the loading was unsuccessful; do not exit program in this class
        Path filePath = null;
        try {
            filePath = Paths.get(file);
            if (!Files.exists(filePath)) {
                printMessageAndExit(MSG_FILE_NOT_FOUND);
            }
        } catch (Exception e) {
            printMessageAndExit(e.getMessage());
        }

        processLines(filePath);
    }

    private void processLines(Path filePath) {
        try (BufferedReader reader = Files.newBufferedReader(filePath, Charset.defaultCharset())) {
            String lineFromFile = "";
            int rowNo = 1;
            while ((lineFromFile = reader.readLine()) != null) {
                if (!lineFromFile.isEmpty()) {
                    if (!paymentProcessor.isInputLineValid(lineFromFile)) {
                        printMessageAndExit("Row number " + rowNo + " in input file is invalid. Program will exit.");
                    }

                    Scanner sc = new Scanner(lineFromFile);
                    paymentProcessor.process(new Payment(sc.next(), sc.nextBigDecimal()));
                }
                rowNo++;
            }
        } catch (Exception e) {
            printMessageAndExit(MSG_ERROR_WHILE_READING + e.getMessage());
        }
    }

    private void printMessageAndExit(String message) {
        System.console().writer().println(message);
        System.exit(-1);
    }
}
