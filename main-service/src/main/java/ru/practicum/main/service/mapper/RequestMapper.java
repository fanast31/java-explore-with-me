package ru.practicum.main.service.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.main.service.dto.request.RequestDto;
import ru.practicum.main.service.model.request.Request;

import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class RequestMapper {
    public static RequestDto toRequestDto(Request request) {
        return RequestDto.builder()
                .id(request.getId())
                .requester(request.getRequester().getId())
                .event(request.getEvent().getId())
                .status(request.getStatus())
                .created(request.getCreated())
                .build();
    }

    public static List<RequestDto> toListRequestDto(List<Request> participationRequestList) {
        return participationRequestList.stream()
                .map(RequestMapper::toRequestDto)
                .collect(Collectors.toList());
    }
}
