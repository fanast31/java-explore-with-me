package ru.practicum.main.service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.main.service.model.User;
import ru.practicum.main.service.model.event.Event;
import ru.practicum.main.service.model.request.Request;
import ru.practicum.main.service.model.request.RequestStatus;

import java.util.List;
import java.util.Optional;

public interface RequestsRepository extends JpaRepository<Request, Long> {
    boolean existsByRequesterAndEventAndStatusNot(
            User requester, Event event, RequestStatus requestStatusList);

    Optional<Request> findByIdAndStatusIn(long requestId, List<RequestStatus> list);

    List<Request> findAllByRequester(User requester);

    List<Request> findAllByEvent(Event event);
}
