package ru.otus.hw.exceptions;

public class NotFoundCostException extends RuntimeException {
    public NotFoundCostException(String message) {
        super(message);
    }
}