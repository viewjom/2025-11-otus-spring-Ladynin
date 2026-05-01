package ru.otus.hw.exceptions;

public class NotFoundTelephoneNumberException extends RuntimeException {
    public NotFoundTelephoneNumberException(String message) {
        super(message);
    }
}
