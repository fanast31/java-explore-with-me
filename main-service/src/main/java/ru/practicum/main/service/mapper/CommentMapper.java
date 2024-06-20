package ru.practicum.main.service.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.main.service.dto.comment.CommentDto;
import ru.practicum.main.service.dto.comment.NewCommentDto;
import ru.practicum.main.service.model.Comment;

import java.time.LocalDateTime;

@UtilityClass
public class CommentMapper {

    public static Comment toComment(NewCommentDto newCommentDto) {
        return Comment.builder()
                .text(newCommentDto.getText())
                .created(LocalDateTime.now())
                .build();
    }

    public static CommentDto toCommentDto(Comment comment) {
        return CommentDto.builder()
                .id(comment.getId())
                .text(comment.getText())
                .authorId(comment.getAuthor().getId())
                .eventId(comment.getEvent().getId())
                .created(comment.getCreated())
                .edited(comment.getEdited())
                .build();
    }

}
