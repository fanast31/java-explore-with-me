package ru.practicum.main.service.controller.publics;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main.service.dto.comment.CommentDto;
import ru.practicum.main.service.servise.comment.CommentService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/comments")
@Slf4j
public class ControllerPublicComment {

    private final CommentService commentService;

    @GetMapping("/searchById/{commentId}")
    public ResponseEntity<CommentDto> getCommentById(@PathVariable Long commentId) {
        log.info("ControllerPublicComment.getCommentById: commentId{}", commentId);
        return ResponseEntity.status(HttpStatus.OK).body(
                commentService.getCommentById(commentId));
    }

    @GetMapping("/searchByEvent/{eventId}")
    public ResponseEntity<List<CommentDto>> getCommentsByEventId(
            @PathVariable Long eventId,
            @RequestParam(defaultValue = "0") Integer from,
            @RequestParam(defaultValue = "10") Integer size) {
        log.info("ControllerPublicComment.getCommentsByEventId: eventId {}, from {}, size {}",
                eventId, from, size);
        return ResponseEntity.status(HttpStatus.OK).body(
                commentService.getCommentsByEventId(eventId, from, size));
    }

    @GetMapping("/searchByParams/{eventId}")
    public ResponseEntity<List<CommentDto>> getAllCommentsByParams(
            @PathVariable Long eventId,
            @RequestParam(required = false) String text,
            @RequestParam(defaultValue = "0") Integer from,
            @RequestParam(defaultValue = "10") Integer size) {
        log.info("ControllerPublicComment.getAllCommentsByParams: eventId {}, text {}, from {}, size {}",
                eventId, text, from, size);
        return ResponseEntity.status(HttpStatus.OK).body(
                commentService.getAllCommentsByParams(eventId, text, from, size));
    }

}
