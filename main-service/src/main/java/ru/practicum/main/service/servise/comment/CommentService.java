package ru.practicum.main.service.servise.comment;

import ru.practicum.main.service.dto.comment.CommentDto;
import ru.practicum.main.service.dto.comment.NewCommentDto;

import java.util.List;

public interface CommentService {
    CommentDto createComment(long userId, long eventId, NewCommentDto newDto);

    CommentDto editComment(long userId, long commentId, NewCommentDto newCommentDto);

    CommentDto getCommentById(long commentId);

    List<CommentDto> getCommentsByEventId(long eventId, int from, int size);

    List<CommentDto> getAllCommentsByParams(long eventId, String text, int from, int size);

    void deleteCommentByCreator(long userId, long commentId);

    void deleteCommentByAdmin(long commentId);
}
