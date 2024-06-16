package ru.practicum.main.service.servise.request;

import ru.practicum.main.service.dto.request.RequestDto;
import ru.practicum.main.service.dto.request.RequestListDto;
import ru.practicum.main.service.dto.request.RequestUpdateStatusDto;

import java.util.List;

public interface RequestService {

    RequestDto createRequest(long userId, long eventId);

    List<RequestDto> getUserRequests(long userId);

    RequestDto cancelRequest(long userId, long requestId);

    RequestListDto updateEventRequests(long userId, long eventId, RequestUpdateStatusDto updateRequest);

}
