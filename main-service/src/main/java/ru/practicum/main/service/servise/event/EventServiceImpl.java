package ru.practicum.main.service.servise.event;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.StatsClient;
import ru.practicum.dto.StatsDtoResponse;
import ru.practicum.main.service.dto.compilation.state.StateActionAdmin;
import ru.practicum.main.service.dto.compilation.state.StateActionUser;
import ru.practicum.main.service.dto.event.EventFullDto;
import ru.practicum.main.service.dto.event.EventShortDto;
import ru.practicum.main.service.dto.event.NewEventDto;
import ru.practicum.main.service.dto.event.UpdateEventRequest;
import ru.practicum.main.service.dto.event.search.AdminSearchEventsParams;
import ru.practicum.main.service.dto.event.search.PublicSearchEventsParams;
import ru.practicum.main.service.dto.request.RequestDto;
import ru.practicum.main.service.exceptions.BadRequestException;
import ru.practicum.main.service.exceptions.ConflictDataException;
import ru.practicum.main.service.exceptions.DataNotFoundException;
import ru.practicum.main.service.mapper.EventMapper;
import ru.practicum.main.service.mapper.LocationMapper;
import ru.practicum.main.service.mapper.RequestMapper;
import ru.practicum.main.service.model.Category;
import ru.practicum.main.service.model.User;
import ru.practicum.main.service.model.event.Event;
import ru.practicum.main.service.model.event.EventState;
import ru.practicum.main.service.model.event.Location;
import ru.practicum.main.service.model.request.Request;
import ru.practicum.main.service.repository.*;
import ru.practicum.main.service.utils.PaginationUtils;

import javax.persistence.criteria.Predicate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    private final EventsRepository eventsRepository;
    private final UserRepository userRepository;
    private final CategoriesRepository categoryRepository;
    private final LocationRepository locationRepository;
    private final RequestsRepository requestsRepository;

    private final StatsClient statsClient;

    @Override
    public EventFullDto createEvent(Long userId, NewEventDto newEventDto) {

        if (newEventDto.getEventDate().isBefore(LocalDateTime.now().plusHours(2))) {
            throw new BadRequestException("EventDate should be before LocalDateTime.now() + 2h");
        }
        User initiator = findUserById(userId);
        Category category = findCategoryById(newEventDto.getCategory());

        Location location = locationRepository.save(LocationMapper.toLocation(newEventDto.getLocation()));

        Event event = EventMapper.toEvent(newEventDto);

        event.setState(EventState.PENDING);
        event.setCategory(category);
        event.setInitiator(initiator);
        event.setLocation(location);
        event.setCreatedOn(LocalDateTime.now());

        return EventMapper.toEventFullDto(eventsRepository.save(event));

    }

    @Override
    @Transactional(readOnly = true)
    public List<EventShortDto> getUserEvents(Long userId, int from, int size) {
        findUserById(userId);
        Pageable pageable = PaginationUtils.createPageable(from, size);
        List<Event> userEvents = eventsRepository.findAllByInitiatorId(userId, pageable);
        return userEvents.stream()
                .map(EventMapper::toEventShortDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public EventFullDto getEvent(long userId, long eventId) {
        findUserById(userId);
        return EventMapper.toEventFullDto(findEventById(eventId));
    }

    @Override
    public EventFullDto updateEvent(Long userId, Long eventId, UpdateEventRequest updateEventRequest) {

        if (updateEventRequest.getEventDate() != null) {
            if (updateEventRequest.getEventDate().isBefore(LocalDateTime.now().plusHours(2))) {
                throw new BadRequestException("EventDate isBefore now + 2h");
            }
        }

        User user = findUserById(userId);
        Event event = findEventById(eventId);

        if (!event.getInitiator().getId().equals(user.getId()))
            throw new ConflictDataException("Only event initiator can update this event");
        if (!event.getState().equals(EventState.PENDING) && !event.getState().equals(EventState.CANCELED))
            throw new ConflictDataException("Only PENDING or CANCELED events can be changed");

        if (updateEventRequest.getCategory() != null) {
            Category category = findCategoryById(updateEventRequest.getCategory());
            event.setCategory(category);
        }

        if (updateEventRequest.getLocation() != null) {
            Location location = LocationMapper.toLocation(updateEventRequest.getLocation());
            Location newEventLocation = locationRepository.findByLatAndLon(
                    location.getLat(),
                    location.getLon()).orElse(locationRepository.save(location));
            event.setLocation(newEventLocation);
        }

        String stateString = updateEventRequest.getStateAction();
        if (stateString != null && !stateString.isBlank()) {
            switch (StateActionUser.valueOf(stateString)) {
                case CANCEL_REVIEW:
                    event.setState(EventState.CANCELED);
                    break;
                case SEND_TO_REVIEW:
                    event.setState(EventState.PENDING);
                    break;
            }
        }

        updateEventFields(event, updateEventRequest);

        return EventMapper.toEventFullDto(eventsRepository.save(event));
    }

    @Override
    @Transactional(readOnly = true)
    public List<RequestDto> getEventRequests(Long userId, Long eventId) {

        findUserById(userId);
        Event event = findEventById(eventId);

        List<Request> eventRequests = requestsRepository.findAllByEvent(event);

        return RequestMapper.toListRequestDto(eventRequests);

    }

    @Override
    @Transactional(readOnly = true)
    public List<EventFullDto> getEventsByAdmin(AdminSearchEventsParams params) {

        LocalDateTime start = params.getRangeStart();
        LocalDateTime end = params.getRangeEnd();

        if (start == null) params.setRangeStart(LocalDateTime.now());

        if (end != null && Objects.requireNonNull(start).isAfter(end))
            throw new ConflictDataException("Start time can't be after End time");

        Pageable pageable = PaginationUtils.createPageable(params.getFrom(), params.getSize());
        Specification<Event> spec = Specification.where(null);

        spec = spec.and((root, query, criteriaBuilder) ->
                criteriaBuilder.greaterThanOrEqualTo(root.get("eventDate"), params.getRangeStart()));

        if (end != null) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.lessThanOrEqualTo(root.get("eventDate"), end));
        }
        if (params.getUsers() != null) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    root.get("initiator").get("id").in(params.getUsers()));
        }

        if (params.getCategories() != null) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    root.get("category").in(params.getCategories()));
        }

        return eventsRepository.findAll(spec, pageable).stream()
                .map(EventMapper::toEventFullDto)
                .collect(Collectors.toList());
    }

    @Override
    public EventFullDto updateEventAdmin(Long eventId, UpdateEventRequest updateDto) {

        Event event = findEventById(eventId);

        if (updateDto.getCategory() != null) {
            long catId = updateDto.getCategory();
            Category category = findCategoryById(catId);
            event.setCategory(category);
        }
        if (updateDto.getEventDate() != null) {
            LocalDateTime actualEventTime = event.getEventDate();
            if (actualEventTime.plusHours(1).isAfter(updateDto.getEventDate()) ||
                    actualEventTime.plusHours(1) != updateDto.getEventDate())
                event.setEventDate(updateDto.getEventDate());

            else
                throw new ConflictDataException("eventDate can't be earlier than one hour from the date of publication.");
        }

        if (updateDto.getLocation() != null) {
            Location location = LocationMapper.toLocation(updateDto.getLocation());
            Location newEventLocation = locationRepository.findByLatAndLon(
                    location.getLat(),
                    location.getLon()).orElse(locationRepository.save(location));
            event.setLocation(newEventLocation);
        }

        if (updateDto.getStateAction() != null) {
            if (event.getState() == EventState.PENDING) {
                StateActionAdmin action = StateActionAdmin.toEnum(updateDto.getStateAction());
                if (action == StateActionAdmin.PUBLISH_EVENT) {
                    event.setState(EventState.PUBLISHED);
                    event.setPublishedOn(LocalDateTime.now());
                } else if (action == StateActionAdmin.REJECT_EVENT) {
                    event.setState(EventState.CANCELED);
                }
            } else {
                throw new ConflictDataException(String.format(
                        "Cannot publish the event because it's not in the right state: %S",
                        event.getState())
                );
            }
        }
        updateEventFields(event, updateDto);
        return EventMapper.toEventFullDto(eventsRepository.save(event));
    }

    @Override
    public List<EventShortDto> getEventsByPublic(PublicSearchEventsParams params) {

        LocalDateTime start = params.getRangeStart();
        LocalDateTime end = params.getRangeEnd();

        if (start == null) params.setRangeStart(LocalDateTime.now());

        if (end != null && Objects.requireNonNull(start).isAfter(end))
            throw new BadRequestException("Start time can't be after End time");

        Specification<Event> spec = (root, query, criteriaBuilder) -> {
            List<Predicate> row = new ArrayList<>();

            if (params.getCategories() != null) {
                row.add(root.get("category").get("id").in(params.getCategories()));
            }

            if (params.getPaid() != null) {
                row.add(criteriaBuilder.equal(root.get("paid"), params.getPaid()));
            }

            if (params.getOnlyAvailable() != null && params.getOnlyAvailable()) {
                row.add(criteriaBuilder.and(
                        criteriaBuilder.greaterThanOrEqualTo(root.get("participantLimit"), 0),
                        criteriaBuilder.lessThan(root.get("participantLimit"), root.get("confirmedRequest"))
                ));
            }
            LocalDateTime current = LocalDateTime.now();
            LocalDateTime startDateTime = Objects.requireNonNullElse(params.getRangeStart(), current);
            row.add(criteriaBuilder.greaterThan(root.get("eventDate"), startDateTime));
            if (params.getRangeEnd() != null) {
                row.add(criteriaBuilder.lessThan(root.get("eventDate"), params.getRangeEnd()));
            }
            if (params.getText() != null && !params.getText().isBlank()) {
                String likeText = "%" + params.getText() + "%";
                row.add(criteriaBuilder.or(
                        criteriaBuilder.like(root.get("annotation"), likeText),
                        criteriaBuilder.like(root.get("description"), likeText)
                ));
            }
            return criteriaBuilder.and(row.toArray(new Predicate[0]));
        };

        int from = params.getFrom(), size = params.getSize();
        Pageable pageable = PageRequest.of(from / size, size);
        if (params.getSort() != null) {
            switch (params.getSort()) {
                case "EVENT_DATE":
                    pageable = PageRequest.of(from / size, size, Sort.Direction.ASC, "eventDate");
                    break;
                case "VIEWS":
                    pageable = PageRequest.of(from / size, size, Sort.Direction.DESC, "views");
                    break;
            }
        }
        List<Event> events = eventsRepository.findAll(spec, pageable);
        if (events.isEmpty()) return new ArrayList<>();

        return EventMapper.toListShortDto(events, getEventsViews(events));
    }

    @Override
    public EventFullDto getEventByPublic(Long eventId) {

        Event event = findEventById(eventId);

        if (!event.getState().equals(EventState.PUBLISHED))
            throw new DataNotFoundException("Event must be published");

        eventsRepository.save(event);

        EventFullDto eventByPublic = EventMapper.toEventFullDto(event);

        eventByPublic.setViews(getEventViews(event));

        return eventByPublic;
    }

    private Long getEventViews(Event event) {
        if (event.getId() != null) {
            List<StatsDtoResponse> eventRequests = statsClient.getStatistics(
                    LocalDateTime.now().minusYears(100),
                    LocalDateTime.now().plusYears(100),
                    Arrays.asList("/events/" + event.getId()),
                    true);

            if (!eventRequests.isEmpty())
                return eventRequests.get(0).getHits();
        }

        return 0L;
    }

    private Map<Long, Long> getEventsViews(List<Event> events) {
        List<String> eventsUri = events.stream()
                .map(e -> "/events/" + e.getId())
                .collect(Collectors.toList());

        List<StatsDtoResponse> eventsRequests = statsClient.getStatistics(
                LocalDateTime.now().minusYears(100),
                LocalDateTime.now().plusYears(100),
                eventsUri,
                true);

        if (!events.isEmpty()) {
            return eventsRequests.stream()
                    .collect(Collectors.toMap(r -> Long.valueOf(r.getUri().substring(8)), StatsDtoResponse::getHits));
        } else {
            return Collections.emptyMap();
        }
    }

    private void updateEventFields(Event savedEvent, UpdateEventRequest newEvent) {

        Map<String, BiConsumer<Event, UpdateEventRequest>> fieldsUpdaters = new HashMap<>();
        fieldsUpdaters.put("annotation",
                (event, eventForUpdate) -> event.setAnnotation(eventForUpdate.getAnnotation()));
        fieldsUpdaters.put("description",
                (event, eventForUpdate) -> event.setDescription(eventForUpdate.getDescription()));
        fieldsUpdaters.put("paid",
                ((event, eventForUpdate) -> event.setPaid(eventForUpdate.getPaid())));
        fieldsUpdaters.put("participantLimit",
                ((event, eventForUpdate) -> event.setParticipantLimit(eventForUpdate.getParticipantLimit())));
        fieldsUpdaters.put("requestModeration",
                ((event, eventForUpdate) -> event.setRequestModeration(eventForUpdate.getRequestModeration())));
        fieldsUpdaters.put("title",
                ((event, eventForUpdate) -> event.setTitle(eventForUpdate.getTitle())));

        fieldsUpdaters.forEach((field, updater) -> {
            switch (field) {
                case "annotation":
                    if (newEvent.getAnnotation() != null) updater.accept(savedEvent, newEvent);
                    break;
                case "description":
                    if (newEvent.getDescription() != null) updater.accept(savedEvent, newEvent);
                    break;
                case "paid":
                    if (newEvent.getPaid() != null) updater.accept(savedEvent, newEvent);
                    break;
                case "participantLimit":
                    if (newEvent.getParticipantLimit() != null) updater.accept(savedEvent, newEvent);
                    break;
                case "requestModeration":
                    if (newEvent.getRequestModeration() != null) updater.accept(savedEvent, newEvent);
                    break;
                case "title":
                    if (newEvent.getTitle() != null) updater.accept(savedEvent, newEvent);
                    break;
            }
        });
    }

    private User findUserById(long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new DataNotFoundException("User isn't found"));
    }

    private Category findCategoryById(long categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new DataNotFoundException("Category isn't found"));
    }

    private Event findEventById(long eventId) {
        return eventsRepository.findById(eventId)
                .orElseThrow(() -> new DataNotFoundException("Event isn't found"));
    }

}
