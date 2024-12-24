package ru.brovkina;

public class Main {
    public static void main(String[] args) {
        ExceptionHandler exceptionHandler = new ExceptionHandler(null);
        FileProcessor processor = new FileProcessor(exceptionHandler);
        AppRunner appRunner = new AppRunner(exceptionHandler, processor);
        appRunner.run(args);
    }
}