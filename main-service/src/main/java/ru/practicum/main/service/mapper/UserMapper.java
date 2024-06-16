package ru.practicum.main.service.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.main.service.dto.user.UserDtoRequest;
import ru.practicum.main.service.dto.user.UserDtoResponse;
import ru.practicum.main.service.model.User;

@UtilityClass
public class UserMapper {

    public static User toUser(UserDtoRequest userDtoRequest) {
        return User.builder()
                .name(userDtoRequest.getName())
                .email(userDtoRequest.getEmail())
                .build();
    }

    public static UserDtoResponse toUserDtoResponse(User user) {
        return UserDtoResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .build();
    }

}
