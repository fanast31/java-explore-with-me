package ru.practicum.main.service.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class ErrorHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse dataNotFoundException(final DataNotFoundException e) {
        log.error("404 NOT_FOUND {}", e.getMessage(), e);
        return new ErrorResponse("DataNotFoundException", e.getMessage());
    }

    @ExceptionHandler({
            MethodArgumentNotValidException.class,
            BadRequestException.class,
            IllegalArgumentException.class,
            MissingServletRequestParameterException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse badRequestException(final Exception e) {
        log.debug("400 BAD_REQUEST {}", e.getMessage());
        return new ErrorResponse("BAD_REQUEST", e.getMessage());
    }

    @ExceptionHandler({
            ConflictDataException.class})
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleConflictDataException(final Exception e) {
        log.debug("Получен статус 409 Conflict {}", e.getMessage(), e);
        return new ErrorResponse("CONFLICT", e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleThrowable(final Throwable e) {
        log.error("500 INTERNAL_SERVER_ERROR {}", e.getMessage(), e);
        return new ErrorResponse("Internal Server Error", e.getMessage());
    }

}
