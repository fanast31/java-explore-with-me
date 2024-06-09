package ru.practicum.stats.server.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.StatsDtoRequest;
import ru.practicum.dto.StatsDtoResponse;
import ru.practicum.stats.server.service.StatsService;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class StatsController {

    private final StatsService statsService;

    @PostMapping("/hit")
    public ResponseEntity<Void> createHit(@Valid @RequestBody StatsDtoRequest statsDtoRequest) {
        log.info("StatsController.createHit (app = {}, uri = {}, ip = {}, timestamp = {})",
                statsDtoRequest.getApp(),
                statsDtoRequest.getUri(),
                statsDtoRequest.getIp(),
                statsDtoRequest.getTimestamp());
        statsService.createHit(statsDtoRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/stats")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<StatsDtoResponse>> getStatistic(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime start,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime end,
            @RequestParam(required = false) List<String> uris,
            @RequestParam(defaultValue = "false") Boolean unique) {
        log.info("StatsController.getStatistic (from = {}, to = {}, uris = {}, unique = {})",
                start,
                end,
                uris,
                unique);
        return ResponseEntity.status(HttpStatus.OK).body(statsService.getStatistic(start, end, uris, unique));
    }

}
