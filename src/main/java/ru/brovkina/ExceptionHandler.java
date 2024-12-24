package ru.brovkina;

public class ExceptionHandler {

    private final  String errorType;

    public ExceptionHandler(String errorType) {
        this.errorType = errorType;
    }

    public void handleException(Exception e, String message, String cause) {
        if (errorType == null) {
            System.err.println("ERROR: " + message + ", cause: " + cause);
        } else {
            System.err.println("ERROR " + errorType + ": " + message + ", cause: " + cause);
        }
    }
    public void logError(String message, Exception e) {
        if (e == null) {
            logError(message);
        } else {
            System.err.println("ERROR: " + message + ", exception message: " + e.getMessage());
        }
    }
    public void logError(String message) {
        System.err.println("ERROR: " + message);
    }
}