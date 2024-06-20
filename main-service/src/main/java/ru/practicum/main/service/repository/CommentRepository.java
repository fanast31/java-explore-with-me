package ru.practicum.main.service.repository;

import io.micrometer.core.lang.Nullable;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.main.service.model.Comment;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAllByEventId(long eventId, Pageable pageable);
    @Query("SELECT c FROM Comment AS c " +
            "WHERE LOWER(c.text) LIKE concat('%',:text,'%') " +
            "AND (c.event.id = :eventId)")
    List<Comment> searchByText(@Nullable String text, Long eventId, Pageable pageable);
}
