package ru.practicum.main.service.controller.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.main.service.servise.comment.CommentService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/comments")
@Slf4j
public class ControllerAdminComment {

    private final CommentService commentService;

    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteCommentByAdmin(@PathVariable long commentId) {
        log.info("ControllerAdminComment.deleteCommentByAdmin: id = {}", commentId);
        commentService.deleteCommentByAdmin(commentId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
