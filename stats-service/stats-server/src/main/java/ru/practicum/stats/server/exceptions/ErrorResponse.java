package ru.practicum.stats.server.exceptions;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {

    private String error;

    private String description;

    public String getError() {
        return error;
    }

    public String getDescription() {
        return description;
    }
}
