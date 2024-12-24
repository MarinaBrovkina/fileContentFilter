package ru.brovkina;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class FileProcessor {
    private String pathForResults = ".";
    private String prefix = "";
    private boolean addMode = false;
    private boolean fullStats = false;
    private final List<String> inputFiles = new ArrayList<>();
    private final ExceptionHandler exceptionHandler;

    public FileProcessor(ExceptionHandler exceptionHandler) {
        this.exceptionHandler = exceptionHandler;
    }

    public void printInfo() {
        System.out.println("Использование: java FileProcessor [-o <выходная директория>] [-p <префикс>] [-a] [-s||-f] <файл1> <файл2> ...");
        System.out.println("Опции:");
        System.out.println("-o <выходная директория> - директория для результатов (по умолчанию текущая)");
        System.out.println("-p <префикс>             - префикс для имен файлов (по умолчанию пусто)");
        System.out.println("-a                       - режим добавления в существующие файлы (по умолчанию перезапись)");
        System.out.println("-s                       - краткая статистика");
        System.out.println("-f                       - полная статистика (по умолчанию краткая)");
        System.out.println("<файл1> <файл2> ...      - входные файлы");
    }

    void parseArguments(String[] args) {
        boolean sFlagFound = false;
        boolean fFlagFound = false;

        for (int i = 0; i < args.length; i++) {
            String arg = args[i];
            switch (arg) {
                case "-o":
                    if (i + 1 < args.length && !args[i + 1].startsWith("-")) {
                        pathForResults = args[++i];
                    } else {
                        throw new IllegalArgumentException("Не указан путь для результатов после -o");
                    }
                    break;
                case "-p":
                    if (i + 1 >= args.length || args[i + 1].startsWith("-")) {
                        throw new IllegalArgumentException("Не указан префикс после -p");
                    }
                    prefix = args[++i];
                    break;
                case "-a":
                    addMode = true;
                    break;
                case "-f":
                    if (sFlagFound) {
                        throw new IllegalArgumentException("Аргументы -f и -s не могут быть указаны одновременно.");
                    }
                    fFlagFound = true;
                    fullStats = true;
                    break;
                case "-s":
                    if (fFlagFound) {
                        throw new IllegalArgumentException("Аргументы -f и -s не могут быть указаны одновременно.");
                    }
                    sFlagFound = true;
                    fullStats = false;
                    break;
                default:
                    if (arg.startsWith("-")) {
                        throw new IllegalArgumentException("Неизвестный аргумент: " + arg);
                    } else {
                        inputFiles.add(arg);
                    }
                    break;
            }
        }
    }

    public boolean isFullStats() {
        return fullStats;
    }

    public boolean hasInputFiles() {
        return !inputFiles.isEmpty();
    }

    public void processFiles() throws IOException {
        Path outputDirPath = Paths.get(pathForResults);
        try {
            if (!Files.exists(outputDirPath)) {
                Files.createDirectories(outputDirPath);
            }
        } catch (IOException e) {
            exceptionHandler.logError("Ошибка при создании директории " + outputDirPath + ": " + e.getMessage(), e);
            return;
        }
        if (hasInputFiles()) {
            Statistics stats = new Statistics();
            DataFilter filter = new DataFilter(exceptionHandler);
            for (String inputFile : inputFiles) {
                try {
                    stats.mergeStatistics(filter.dataProcessing(inputFile));
                } catch (IOException e) {
                    exceptionHandler.logError("Ошибка ввода-вывода при обработке файла " + inputFile + ": " + e.getMessage(), e);
                } catch (Exception e) {
                    exceptionHandler.logError("Непредвиденная ошибка при обработке файла " + inputFile + ": " + e.getMessage(), e);
                }
            }
            stats.printStatistics(isFullStats());
            try {
                stats.saveToFile(pathForResults, prefix, addMode);
            } catch (IOException e) {
                exceptionHandler.logError("Ошибка сохранения результатов в файл: " + e.getMessage(), e);
            }
        }
    }
}