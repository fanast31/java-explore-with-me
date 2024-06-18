package ru.practicum.main.service.exceptions;

public class ConflictDataException extends RuntimeException {

    public ConflictDataException(String message) {
        super(message);
    }
}
