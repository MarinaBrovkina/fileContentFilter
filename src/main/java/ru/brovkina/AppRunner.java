package ru.brovkina;

import java.io.IOException;

public class AppRunner {
    private static final String FILES_PROCESSED = "Файлы успешно обработаны";
    private static final String ARGUMENTS_PARSED = "Аргументы успешно разобраны";

    private final ExceptionHandler exceptionHandler;
    private final FileProcessor processor;

    public AppRunner(ExceptionHandler exceptionHandler, FileProcessor processor) {
        this.exceptionHandler = exceptionHandler;
        this.processor = processor;
    }

    private void logInfo(String message) {
        if (System.getProperty("log.level", "ERROR").equalsIgnoreCase
                ("INFO") || System.getProperty("log.level", "ERROR")
                .equalsIgnoreCase("DEBUG")) {
            System.err.println("INFO: " + message);
        }
    }

    private void logDebug(String message) {
        if (System.getProperty("log.level", "ERROR")
                .equalsIgnoreCase("DEBUG")) {
            System.err.println("DEBUG: " + message);
        }
    }

    private void logError(String message) {
        if (processor != null) {
            processor.printInfo();
        }
        exceptionHandler.logError(message);
    }

    public void run(String[] args) {
        try {
            processor.parseArguments(args);
            logDebug(ARGUMENTS_PARSED);
            try {
                processor.processFiles();
                if (processor.hasInputFiles()) {
                    logInfo(FILES_PROCESSED);
                } else {
                    logError("Не указаны входные файлы");
                }
            } catch (IOException e) {
                logError("Ошибка ввода вывода: " + e.getMessage());
            }
        } catch (IllegalArgumentException e) {
            exceptionHandler.handleException(e, "Ошибка в аргументах: " + e.getMessage(), "parsing arguments");
            if (processor.hasInputFiles()) {
                try {
                    processor.processFiles();
                } catch (IOException ex) {
                    logError("Ошибка ввода вывода: " + ex.getMessage());
                }
            }
        }
    }
}

