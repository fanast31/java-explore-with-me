package ru.practicum.main.service.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.main.service.dto.event.EventFullDto;
import ru.practicum.main.service.dto.event.EventShortDto;
import ru.practicum.main.service.dto.event.NewEventDto;
import ru.practicum.main.service.model.event.Event;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@UtilityClass
public class EventMapper {

    public static Event toEvent(NewEventDto newEventDto) {
        return Event.builder()
                .annotation(newEventDto.getAnnotation())
                .description(newEventDto.getDescription())
                .eventDate(newEventDto.getEventDate())
                .paid(newEventDto.getPaid())
                .participantLimit(newEventDto.getParticipantLimit())
                .requestModeration(newEventDto.getRequestModeration())
                .title(newEventDto.getTitle())
                .build();
    }

    public static EventFullDto toEventFullDto(Event event) {
        return EventFullDto.builder()
                .id(event.getId())
                .annotation(event.getAnnotation())
                .confirmedRequests(event.getConfirmedRequests())
                .createdOn(event.getCreatedOn())
                .description(event.getDescription())
                .eventDate(event.getEventDate())
                .paid(event.getPaid())
                .participantLimit(event.getParticipantLimit())
                .publishedOn(event.getPublishedOn())
                .requestModeration(event.getRequestModeration())
                .title(event.getTitle())
                .views(event.getViews())

                .state(event.getState())
                .category((CategoryMapper.toCategoryDtoResponse(event.getCategory())))
                .initiator(UserMapper.toUserDtoResponse(event.getInitiator()))
                .location(event.getLocation())
                .build();
    }

    public static EventShortDto toEventShortDto(Event event) {
        return EventShortDto.builder()
                .id(event.getId())
                .annotation(event.getAnnotation())
                .confirmedRequests(event.getConfirmedRequests())
                .eventDate(event.getEventDate())
                .paid(event.getPaid())
                .title(event.getTitle())
                .views(event.getViews())

                .category(CategoryMapper.toCategoryDtoResponse(event.getCategory()))
                .initiator(UserMapper.toUserDtoResponse(event.getInitiator()))
                .build();
    }

    public static List<EventShortDto> toListShortDto(List<Event> events) {
        return events.stream()
                .map(EventMapper::toEventShortDto)
                .collect(Collectors.toList());
    }
}
