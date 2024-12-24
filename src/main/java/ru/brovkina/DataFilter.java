package ru.brovkina;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

class DataFilter {

    private final ExceptionHandler exceptionHandler;

    public DataFilter(ExceptionHandler exceptionHandler) {
        this.exceptionHandler = exceptionHandler;
    }
    public Statistics dataProcessing(String filename) throws IOException {
        Statistics stats = new Statistics();
        File file = new File(filename);
        if (!file.exists() || !file.isFile()) {
            exceptionHandler.logError("Файл не найден: " + filename);
            return stats;
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                try {
                    int intValue = Integer.parseInt(line);
                    stats.addValue(intValue, "integer");
                } catch (NumberFormatException e) {
                    try {
                        float floatValue = Float.parseFloat(line);
                        stats.addValue(floatValue, "float");
                    } catch (NumberFormatException e2) {
                        stats.addValue(line, "string");
                    }
                }
            }
        } catch (IOException e) {
            exceptionHandler.logError("Ошибка чтения файла " + filename + ": " + e.getMessage(), e);
        }
        return stats;
    }
}