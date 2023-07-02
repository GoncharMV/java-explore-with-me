package ru.practicum.utils.handler;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.utils.exception.BadRequestException;
import ru.practicum.utils.exception.ObjectNotFoundException;
import ru.practicum.utils.exception.RequestNotProcessedException;

import java.util.Map;

@RestControllerAdvice
public class CustomErrorHandler {

    @ExceptionHandler
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public Map<String, String> handleObjectNotFound(final ObjectNotFoundException e) {
        return Map.of("error", e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(value = HttpStatus.CONFLICT)
    public Map<String, String> handleRequestNotProcessed(final RequestNotProcessedException e) {
        return Map.of("error", e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public Map<String, String> handleBadRequest(final BadRequestException e) {
        return Map.of("error", e.getMessage());
    }

}
