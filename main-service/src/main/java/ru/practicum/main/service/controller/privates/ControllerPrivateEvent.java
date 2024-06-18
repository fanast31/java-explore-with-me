package ru.practicum.main.service.controller.privates;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main.service.dto.event.EventFullDto;
import ru.practicum.main.service.dto.event.EventShortDto;
import ru.practicum.main.service.dto.event.NewEventDto;
import ru.practicum.main.service.dto.event.UpdateEventRequest;
import ru.practicum.main.service.servise.event.EventService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Slf4j
public class ControllerPrivateEvent {

    private final EventService eventService;

    @PostMapping("/{userId}/events")
    public ResponseEntity<EventFullDto> createEvent(
            @PathVariable Long userId,
            @Valid @RequestBody NewEventDto newEventDto) {
        log.info("ControllerPrivateEvent.postEvent (userId = {}, event newEventDto = {})", userId, newEventDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(eventService.createEvent(userId, newEventDto));
    }

    @PatchMapping("/{userId}/events/{eventId}")
    public ResponseEntity<EventFullDto> updateEvent(
            @PathVariable Long userId,
            @PathVariable Long eventId,
            @Valid @RequestBody UpdateEventRequest updateEventRequest) {
        log.debug("ControllerPrivateEvent.updateEvent {}, by user id: {}", eventId, userId);
        return ResponseEntity.status(HttpStatus.OK).body(eventService.updateEvent(userId, eventId, updateEventRequest));
    }

    @GetMapping("/{userId}/events")
    public ResponseEntity<List<EventShortDto>> getUserEvents(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") @PositiveOrZero int from,
            @RequestParam(defaultValue = "10") @Positive int size) {
        log.info("ControllerPrivateEvent.getEvents (userId = {}, from = {}, size = {})", userId, from, size);
        return ResponseEntity.status(HttpStatus.OK).body(eventService.getUserEvents(userId, from, size));
    }

    @GetMapping("/{userId}/events/{eventId}")
    public ResponseEntity<EventFullDto> getEvent(
            @PathVariable Long userId,
            @PathVariable Long eventId) {
        log.info("ControllerPrivateEvent.getEvent (userId = {}, eventId = {})", userId, eventId);
        return ResponseEntity.status(HttpStatus.OK).body(eventService.getEvent(userId, eventId));
    }

}