package ru.practicum.main.service.controller.privates;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main.service.dto.comment.CommentDto;
import ru.practicum.main.service.dto.comment.NewCommentDto;
import ru.practicum.main.service.servise.comment.CommentService;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users/{userId}/comments")
@Slf4j
public class ControllerPrivateComment {

    private final CommentService commentService;

    @PostMapping("/{eventId}")
    public ResponseEntity<CommentDto> createComment(
            @PathVariable Long userId,
            @PathVariable Long eventId,
            @RequestBody @Valid NewCommentDto newCommentDto) {
        log.info("ControllerPrivateComment.createComment: userId {}, eventId {}, newCommentDto {}",
                userId, eventId, newCommentDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                commentService.createComment(userId, eventId, newCommentDto));
    }

    @PatchMapping("/{commentId}")
    public ResponseEntity<CommentDto> editComment(
            @PathVariable Long userId,
            @PathVariable Long commentId,
            @RequestBody @Valid NewCommentDto newCommentDto) {
        log.info("ControllerPrivateComment.editComment: userId {}, commentId{}, newCommentDto {}",
                userId, commentId, newCommentDto);
        return ResponseEntity.status(HttpStatus.OK).body(
                commentService.editComment(userId, commentId, newCommentDto));
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteCommentByOwner(
            @PathVariable Long userId,
            @PathVariable Long commentId) {
        log.info("ControllerPrivateComment.deleteCommentByOwner: userId {}, commentId {}", userId, commentId);
        commentService.deleteCommentByCreator(userId, commentId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
