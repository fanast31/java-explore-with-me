package ru.practicum.main.service.servise.request;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.main.service.dto.request.RequestDto;
import ru.practicum.main.service.dto.request.RequestListDto;
import ru.practicum.main.service.dto.request.RequestUpdateStatusDto;
import ru.practicum.main.service.exceptions.ConflictDataException;
import ru.practicum.main.service.exceptions.DataNotFoundException;
import ru.practicum.main.service.mapper.RequestMapper;
import ru.practicum.main.service.model.User;
import ru.practicum.main.service.model.event.Event;
import ru.practicum.main.service.model.event.EventState;
import ru.practicum.main.service.model.request.Request;
import ru.practicum.main.service.model.request.RequestStatus;
import ru.practicum.main.service.repository.EventsRepository;
import ru.practicum.main.service.repository.RequestsRepository;
import ru.practicum.main.service.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class RequestServiceImpl implements RequestService {

    private final RequestsRepository requestRepository;
    private final EventsRepository eventsRepository;
    private final UserRepository userRepository;

    @Override
    public RequestDto createRequest(long userId, long eventId) {

        User user = findUserById(userId);
        Event event = findEventById(eventId);

        if (!event.getState().equals(EventState.PUBLISHED))
            throw new ConflictDataException("Event not published");

        if (requestRepository.existsByRequesterAndEventAndStatusNot(
                user, event, RequestStatus.CANCELED)) {
            throw new ConflictDataException("User already registered");
        }
        if (event.getInitiator().equals(user))
            throw new ConflictDataException("User is initiator");

        if ((event.getConfirmedRequests() >= event.getParticipantLimit())
                && (event.getConfirmedRequests() != 0 && event.getParticipantLimit() != 0)) {
            throw new ConflictDataException("Participation limit is overflowed");
        }

        Request request = Request.builder()
                .created(LocalDateTime.now())
                .status(RequestStatus.PENDING)
                .event(event)
                .requester(user)
                .build();

        if (!event.getRequestModeration() || event.getParticipantLimit() == 0) {
            request.setStatus(RequestStatus.CONFIRMED);
            event.setConfirmedRequests(event.getConfirmedRequests() + 1);
        }

        return RequestMapper.toRequestDto(requestRepository.save(request));

    }

    @Override
    public RequestListDto updateEventRequests(long userId, long eventId, RequestUpdateStatusDto updateRequest) {

        User user = findUserById(userId);
        Event event = findEventById(eventId);

        if (!event.getInitiator().getId().equals(user.getId()))
            throw new ConflictDataException("Only event initiator can update this event");

        if (event.getConfirmedRequests() >= event.getParticipantLimit())
            throw new ConflictDataException("Event's participant limit is full");

        if (!event.getState().equals(EventState.PUBLISHED)) {
            throw new ConflictDataException("Participation  status is not pending");
        }

        List<Request> requests = requestRepository.findAllById(updateRequest.getRequestIds());

        List<RequestDto> confirmedRequest = new ArrayList<>();
        List<RequestDto> rejectedRequest = new ArrayList<>();

        boolean isEventChanged = false;
        for (Request request : requests) {
            if (updateRequest.getStatus().equals(RequestStatus.CONFIRMED)) {
                if (event.getParticipantLimit() == 0 || !event.getRequestModeration()) {
                    request.setStatus(RequestStatus.CONFIRMED);
                    confirmedRequest.add(RequestMapper.toRequestDto(request));
                } else if (event.getConfirmedRequests() < event.getParticipantLimit()) {
                    request.setStatus(RequestStatus.CONFIRMED);
                    event.setConfirmedRequests(event.getConfirmedRequests() + 1);
                    isEventChanged = true;
                    confirmedRequest.add(RequestMapper.toRequestDto(request));
                } else {
                    request.setStatus(RequestStatus.REJECTED);
                    rejectedRequest.add(RequestMapper.toRequestDto(request));
                }
            } else {
                request.setStatus(RequestStatus.REJECTED);
                rejectedRequest.add(RequestMapper.toRequestDto(request));
            }
            requestRepository.save(request);
        }

        if (isEventChanged) eventsRepository.save(event);

        return RequestListDto.builder()
                .confirmedRequests(confirmedRequest)
                .rejectedRequests(rejectedRequest)
                .build();
    }

    @Override
    public RequestDto cancelRequest(long userId, long requestId) {
        findUserById(userId);
        Request request = requestRepository.findByIdAndStatusIn(
                        requestId, Arrays.asList(RequestStatus.PENDING, RequestStatus.CONFIRMED))
                .orElseThrow(() -> new DataNotFoundException("Request not found"));
        request.setStatus(RequestStatus.CANCELED);
        return RequestMapper.toRequestDto(requestRepository.save(request));
    }

    @Override
    @Transactional(readOnly = true)
    public List<RequestDto> getUserRequests(long userId) {
        User user = findUserById(userId);
        List<Request> requests = requestRepository.findAllByRequester(user);
        return requests.stream()
                .map(RequestMapper::toRequestDto)
                .collect(Collectors.toList());
    }


    private User findUserById(long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new DataNotFoundException("User isn't found"));
    }

    private Event findEventById(long eventId) {
        return eventsRepository.findById(eventId)
                .orElseThrow(() -> new DataNotFoundException("Event isn't found"));
    }

}
