package ru.practicum.main.service.controller.privates;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main.service.dto.request.RequestDto;
import ru.practicum.main.service.dto.request.RequestListDto;
import ru.practicum.main.service.dto.request.RequestUpdateStatusDto;
import ru.practicum.main.service.servise.event.EventService;
import ru.practicum.main.service.servise.request.RequestService;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Slf4j
public class ControllerPrivateRequests {

    private final RequestService requestService;
    private final EventService eventService;

    @PostMapping("/{userId}/requests")
    public ResponseEntity<RequestDto> createRequest(
            @PathVariable Long userId,
            @RequestParam Long eventId) {
        log.info("ControllerPrivateRequests.createRequest userId: {} eventId: {}", userId, eventId);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                requestService.createRequest(userId, eventId));
    }

    @PatchMapping("/{userId}/events/{eventId}/requests")
    public ResponseEntity<RequestListDto> updateEventRequests(
            @PathVariable long userId,
            @PathVariable long eventId,
            @RequestBody RequestUpdateStatusDto updateRequest) {
        log.info("ControllerPrivateRequests.updateEventRequests userId = {}, eventId = {}, updateRequest = {}",
                userId, eventId, updateRequest);
        return ResponseEntity.status(HttpStatus.OK).body(
                requestService.updateEventRequests(userId, eventId, updateRequest));
    }

    @PatchMapping("/{userId}/requests/{requestId}/cancel")
    public ResponseEntity<RequestDto> cancelRequest(
            @PathVariable long userId,
            @PathVariable long requestId) {
        log.info("ControllerPrivateRequests.cancelRequest userId: {} requestId: {}", userId, requestId);
        return ResponseEntity.status(HttpStatus.OK).body(
                requestService.cancelRequest(userId, requestId));
    }

    @GetMapping("/{userId}/requests")
    public ResponseEntity<List<RequestDto>> getRequest(
            @PathVariable long userId) {
        log.info("ControllerPrivateRequests.getRequest userId: {}", userId);
        return ResponseEntity.status(HttpStatus.OK).body(
                requestService.getUserRequests(userId));
    }

    @GetMapping("/{userId}/events/{eventId}/requests")
    public ResponseEntity<List<RequestDto>> getEventRequests(
            @PathVariable long userId,
            @PathVariable long eventId) {
        log.info("ControllerPrivateRequests.getEventRequests userId: {} eventId: {}", userId, eventId);
        return ResponseEntity.status(HttpStatus.OK).body(
                eventService.getEventRequests(userId, eventId));
    }

}