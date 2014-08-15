package com.bsc.paymenttracker.io.console;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Singleton ConsoleReader reads user line input from console. It creates task waiting for user input. There is always
 * just one task created.
 *
 * @author Milos Klvan
 */
public class ConsoleReader {
    private static ConsoleReader instance = new ConsoleReader();

    private Future<String> future;
    private ExecutorService ex;

    private ConsoleReader() {}

    public static ConsoleReader getInstance() {
        return instance;
    }

    private Future<String> createFuture() {
        ex = Executors.newSingleThreadExecutor();
        return ex.submit(new ConsoleReaderTask());
    }

    private void closeFuture() {
        if (future == null) {
            return;
        }
        ex.shutdownNow();
        future = null;
    }

    /**
     * Checks if user entered some line input to the console.
     * @return input entered by user or null if nothing was entered
     * @throws InterruptedException
     * @throws ExecutionException
     */
    public synchronized String readLine() throws InterruptedException, ExecutionException {
        if (future == null) {
            future = createFuture();
        }

        String input = null;

        try {
            if (future.isDone()) {
                input = future.get();
            }
        } finally {
            if (input != null) closeFuture();
        }

        return input;
    }
}
