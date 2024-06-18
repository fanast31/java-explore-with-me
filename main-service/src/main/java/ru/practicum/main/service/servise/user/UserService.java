package ru.practicum.main.service.servise.user;

import ru.practicum.main.service.dto.user.UserDtoRequest;
import ru.practicum.main.service.dto.user.UserDtoResponse;

import java.util.List;

public interface UserService {
    UserDtoResponse create(UserDtoRequest userDtoRequest);

    List<UserDtoResponse> getAll(List<Long> ids, int from, int size);

    void delete(long userId);

}
