package ru.practicum.main.service.servise.event;

import ru.practicum.main.service.dto.event.EventFullDto;
import ru.practicum.main.service.dto.event.EventShortDto;
import ru.practicum.main.service.dto.event.NewEventDto;
import ru.practicum.main.service.dto.event.UpdateEventRequest;
import ru.practicum.main.service.dto.event.search.AdminSearchEventsParams;
import ru.practicum.main.service.dto.event.search.PublicSearchEventsParams;
import ru.practicum.main.service.dto.request.RequestDto;

import java.util.List;

public interface EventService {

    EventFullDto createEvent(Long userId, NewEventDto newEventDto);

    List<EventShortDto> getUserEvents(Long userId, int from, int size);

    EventFullDto getEvent(long userId, long eventId);

    EventFullDto updateEvent(Long userId, Long eventId, UpdateEventRequest newEventDto);

    List<RequestDto> getEventRequests(Long userId, Long eventId);

    List<EventFullDto> getEventsByAdmin(AdminSearchEventsParams params);

    EventFullDto updateEventAdmin(Long eventId, UpdateEventRequest updateDtoEventRequest);

    List<EventShortDto> getEventsByPublic(PublicSearchEventsParams params);

    EventFullDto getEventByPublic(Long eventId);

}
