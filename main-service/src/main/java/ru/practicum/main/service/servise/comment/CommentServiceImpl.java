package ru.practicum.main.service.servise.comment;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.main.service.dto.comment.CommentDto;
import ru.practicum.main.service.dto.comment.NewCommentDto;
import ru.practicum.main.service.exceptions.ConflictDataException;
import ru.practicum.main.service.exceptions.DataNotFoundException;
import ru.practicum.main.service.mapper.CommentMapper;
import ru.practicum.main.service.model.Comment;
import ru.practicum.main.service.model.User;
import ru.practicum.main.service.model.event.Event;
import ru.practicum.main.service.model.event.EventState;
import ru.practicum.main.service.repository.CommentRepository;
import ru.practicum.main.service.repository.EventsRepository;
import ru.practicum.main.service.repository.UserRepository;
import ru.practicum.main.service.utils.PaginationUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final EventsRepository eventRepository;

    @Override
    public CommentDto createComment(long userId, long eventId, NewCommentDto newDto) {

        User author = findUserById(userId);
        Event event = findEventById(eventId);
        if (event.getState() != EventState.PUBLISHED) {
            throw new ConflictDataException("Event.state <> PUBLISHED");
        }

        Comment comment = CommentMapper.toComment(newDto);
        comment.setAuthor(author);
        comment.setEvent(event);

        return CommentMapper.toCommentDto(commentRepository.save(comment));

    }

    @Override
    public CommentDto editComment(long userId, long commentId, NewCommentDto newCommentDto) {

        findUserById(userId);
        Comment comment = findCommentById(commentId);
        if (comment.getEvent().getState() != EventState.PUBLISHED) {
            throw new ConflictDataException("Event.state <> PUBLISHED");
        }

        if (!comment.getText().equals(newCommentDto.getText())) {
            comment.setText(newCommentDto.getText());
            comment.setEdited(LocalDateTime.now());
            comment = commentRepository.save(comment);
        }
        return CommentMapper.toCommentDto(comment);

    }

    @Override
    @Transactional(readOnly = true)
    public CommentDto getCommentById(long commentId) {
        return CommentMapper.toCommentDto(findCommentById(commentId));
    }

    @Override
    @Transactional(readOnly = true)
    public List<CommentDto> getCommentsByEventId(long eventId, int from, int size) {
        Pageable pageable = PaginationUtils.createPageable(from, size, PaginationUtils.SORT_CREATED_DESC);
        return commentRepository.findAllByEventId(eventId, pageable).stream()
                .map(CommentMapper::toCommentDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @Override
    public List<CommentDto> getAllCommentsByParams(long eventId, String text, int from, int size) {
        if (text != null && !text.trim().isEmpty()) {
            Pageable pageable = PaginationUtils.createPageable(from, size, PaginationUtils.SORT_CREATED_DESC);
            findEventById(eventId);

            return commentRepository.searchByText(text.toLowerCase(), eventId, pageable).stream()
                    .map(CommentMapper::toCommentDto)
                    .collect(Collectors.toList());
        }
        return List.of();
    }

    @Override
    public void deleteCommentByAdmin(long commentId) {
        Comment comment = findCommentById(commentId);
        commentRepository.delete(comment);
    }

    @Override
    public void deleteCommentByCreator(long userId, long commentId) {
        Comment comment = findCommentById(commentId);
        User creator = findUserById(userId);
        if (Objects.equals(comment.getAuthor(), creator)) {
            commentRepository.delete(comment);
        } else {
            throw new ConflictDataException("User should be = Comment.author");
        }
    }

    private User findUserById(long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new DataNotFoundException("User wasn't found"));
    }

    private Comment findCommentById(long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new DataNotFoundException("Comment wasn't found"));
    }

    private Event findEventById(long eventId) {
        return eventRepository.findById(eventId)
                .orElseThrow(() -> new DataNotFoundException("Event wasn't found"));
    }

}
