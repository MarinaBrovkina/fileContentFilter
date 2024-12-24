package ru.brovkina;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Statistics {
    private final List<Integer> integers = new ArrayList<>();
    private final List<Float> floats = new ArrayList<>();
    private final List<String> strings = new ArrayList<>();

    public void addValue(Object value, String type) {
        if (type.equals("integer")) {
            integers.add((Integer) value);
        } else if (type.equals("float")) {
            floats.add((Float) value);
        } else {
            strings.add((String) value);
        }
    }

    public void mergeStatistics(Statistics other) {
        integers.addAll(other.integers);
        floats.addAll(other.floats);
        strings.addAll(other.strings);
    }

    public void printStatistics(boolean fullStats) {
        System.out.println("Статистика:");
        printStatHelper(integers, "Целые числа", fullStats);
        printStatHelper(floats, "Числа с плавающей точкой", fullStats);
        printStatHelper(strings, "Строки", fullStats);
    }

    private void printStatHelper(List<?> list, String label, boolean fullStats) {
        if (list.isEmpty()) {
            System.out.println(label + ": пусто");
            return;
        }
        System.out.print(label + ": ");
        if (fullStats) {
            if (list.getFirst().getClass() == Integer.class) {
                printFullStatsInteger((List<Integer>) list);
            } else if (list.getFirst().getClass() == Float.class) {
                printFullStatsFloat((List<Float>) list);
            } else if (list.getFirst().getClass() == String.class) {
                printFullStatsString((List<String>) list);
            }
        } else {
            System.out.println(list.size());
        }
    }

    private void printFullStatsInteger(List<Integer> values) {
        if (values.isEmpty()) {
            System.out.println("пусто");
        } else {
            System.out.println("min: " + Collections.min(values) +
                    ", max: " + Collections.max(values) + ", sum: " + values.stream().mapToInt(Integer::intValue).sum() +
                    ", avg: " + (double) values.stream().mapToInt(Integer::intValue).sum() / values.size());
        }
    }

    private void printFullStatsFloat(List<Float> values) {
        if (values.isEmpty()) {
            System.out.println("пусто");
        } else {
            System.out.println("min: " + Collections.min(values) +
                    ", max: " + Collections.max(values) + ", sum: " + values.stream().mapToDouble(Float::doubleValue).sum() +
                    ", avg: " + values.stream().mapToDouble(Float::doubleValue).average().orElse(0));
        }

    }

    private void printFullStatsString(List<String> values) {
        if (values.isEmpty()) {
            System.out.println("пусто");
        } else {
            System.out.println("min length: " + Collections.min(values, java.util.Comparator.comparingInt(String::length)).length() +
                    ", max length: " + Collections.max(values, java.util.Comparator.comparingInt(String::length)).length());
        }
    }

    public void saveToFile(String pathForResults, String prefix, boolean append) throws IOException {
        saveToFileHelper(integers, pathForResults, prefix + "integers.txt", append);
        saveToFileHelper(floats, pathForResults, prefix + "floats.txt", append);
        saveToFileHelper(strings, pathForResults, prefix + "strings.txt", append);

    }

    private <T> void saveToFileHelper(List<T> data, String path, String filename, boolean append) throws IOException {
        if (!data.isEmpty()) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(path + File.separator + filename, append))) {
                for (T item : data) {
                    writer.write(item.toString());
                    writer.newLine();
                }
            }
        }
    }
}