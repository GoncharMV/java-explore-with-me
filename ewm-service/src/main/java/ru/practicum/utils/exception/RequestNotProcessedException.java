package ru.practicum.utils.exception;

public class RequestNotProcessedException extends RuntimeException {

    public RequestNotProcessedException(String message) {
        super(message);
    }
}
