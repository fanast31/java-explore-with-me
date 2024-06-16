package ru.practicum.main.service.controller.publics;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.StatsClient;
import ru.practicum.main.service.dto.event.EventFullDto;
import ru.practicum.main.service.dto.event.EventShortDto;
import ru.practicum.main.service.dto.event.search.PublicSearchEventsParams;
import ru.practicum.main.service.servise.event.EventService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
@Slf4j
public class ControllerPublicEvent {

    private final EventService eventService;
    private final StatsClient statsClient;

    @Value("${app}")
    public String app;

    @GetMapping
    public ResponseEntity<List<EventShortDto>> getEventsBySearch(
            @RequestParam(required = false) String text,
            @RequestParam(required = false) List<Long> categories,
            @RequestParam(required = false) Boolean paid,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeStart,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeEnd,
            @RequestParam(required = false) Boolean onlyAvailable,
            @RequestParam(required = false) String sort,
            @RequestParam(defaultValue = "0") @PositiveOrZero int from,
            @RequestParam(defaultValue = "10") @Positive int size,
            HttpServletRequest httpServletRequest) {
        log.info("ControllerPublicEvent.getEventsBySearch (text = {}, categories = {}, " +
                        "paid = {}, rangeStart = {}, rangeEnd = {}, " +
                        "onlyAvailable = {}, sort = {}, from = {}, size = {})",
                text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort, from, size);

        PublicSearchEventsParams params = PublicSearchEventsParams.builder()
                .text(text)
                .categories(categories)
                .paid(paid)
                .rangeStart(rangeStart)
                .rangeEnd(rangeEnd)
                .onlyAvailable(onlyAvailable)
                .sort(sort)
                .from(from)
                .size(size)
                .build();

        statsClient.createHit(app, httpServletRequest);

        return ResponseEntity.status(HttpStatus.OK).body(eventService.getEventsByPublic(params));

    }

    @GetMapping("/{id}")
    public ResponseEntity<EventFullDto> getEventByPublic(
            @PathVariable(value = "id") Long eventId,
            HttpServletRequest httpServletRequest) {

        log.info("ControllerPublicEvent.getEventsBySearch (eventId = {})", eventId);

        statsClient.createHit(app, httpServletRequest);

        return ResponseEntity.status(HttpStatus.OK).body(eventService.getEventByPublic(eventId));

    }

}