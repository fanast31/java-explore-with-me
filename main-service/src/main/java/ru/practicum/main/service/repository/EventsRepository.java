package ru.practicum.main.service.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.main.service.model.event.Event;

import java.util.List;

public interface EventsRepository extends JpaRepository<Event, Long> {
    List<Event> findAllByInitiatorId(long userId, Pageable pageable);

    List<Event> findAll(Specification<Event> specification, Pageable pageable);
}
