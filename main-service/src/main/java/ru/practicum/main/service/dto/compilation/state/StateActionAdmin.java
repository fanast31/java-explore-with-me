package ru.practicum.main.service.dto.compilation.state;

import ru.practicum.main.service.exceptions.BadRequestException;

public enum StateActionAdmin {
    PUBLISH_EVENT,
    REJECT_EVENT;

    public static StateActionAdmin toEnum(String action) {
        try {
            return StateActionAdmin.valueOf(action);
        } catch (RuntimeException exception) {
            throw new BadRequestException("Bad status: " + action);
        }
    }
}