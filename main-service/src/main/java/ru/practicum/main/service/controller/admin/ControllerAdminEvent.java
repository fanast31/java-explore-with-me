package ru.practicum.main.service.controller.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main.service.dto.event.EventFullDto;
import ru.practicum.main.service.dto.event.UpdateEventRequest;
import ru.practicum.main.service.dto.event.search.AdminSearchEventsParams;
import ru.practicum.main.service.servise.event.EventService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/admin/events")
@RequiredArgsConstructor
@Slf4j
public class ControllerAdminEvent {

    private final EventService eventService;

    @GetMapping
    public ResponseEntity<List<EventFullDto>> getEventsByAdmin(
            @RequestParam(required = false) List<Long> users,
            @RequestParam(required = false) List<String> states,
            @RequestParam(required = false) List<Long> categories,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeStart,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeEnd,
            @RequestParam(defaultValue = "0") @PositiveOrZero int from,
            @RequestParam(defaultValue = "10") @Positive int size) {
        AdminSearchEventsParams params = AdminSearchEventsParams.builder()
                .users(users)
                .states(states)
                .categories(categories)
                .rangeStart(rangeStart)
                .rangeEnd(rangeEnd)
                .from(from)
                .size(size)
                .build();
        log.info("EventAdminController.getEvents {}", params);
        return ResponseEntity.status(HttpStatus.OK).body(eventService.getEventsByAdmin(params));
    }

    @PatchMapping("/{eventId}")
    public ResponseEntity<EventFullDto> updateEventAdmin(
            @PathVariable Long eventId,
            @RequestBody @Valid UpdateEventRequest updateEventRequest) {
        log.info("EventAdminController.Patch: {}", eventId);
        return ResponseEntity.status(HttpStatus.OK).body(eventService.updateEventAdmin(eventId, updateEventRequest));
    }

}
