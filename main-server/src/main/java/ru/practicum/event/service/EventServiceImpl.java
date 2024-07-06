package ru.practicum.event.service;

import com.querydsl.core.types.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.StatsClientImpl;
import ru.practicum.StatsDtoIn;
import ru.practicum.StatsDtoOut;
import ru.practicum.category.dao.CategoryDAO;
import ru.practicum.category.model.Category;
import ru.practicum.event.dao.EventDAO;
import ru.practicum.event.dto.*;
import ru.practicum.event.mapper.EventMapper;
import ru.practicum.event.model.*;
import ru.practicum.exception.BadRequestException;
import ru.practicum.exception.DataConflictException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.request.dao.RequestDAO;
import ru.practicum.request.dto.ParticipationDtoOut;
import ru.practicum.request.mapper.RequestMapper;
import ru.practicum.request.model.Request;
import ru.practicum.request.model.RequestStatus;
import ru.practicum.user.dao.UserDAO;
import ru.practicum.util.EwmPageRequest;
import ru.practicum.util.QPredicateBuilder;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import static ru.practicum.constant.Constant.DATE_TIME_PATTERN;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EventServiceImpl implements EventService {

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_TIME_PATTERN);
    private final EventDAO eventDAO;
    private final RequestDAO requestDAO;
    private final UserDAO userDAO;
    private final CategoryDAO categoryDAO;
    private final EventMapper eventMapper;
    private final RequestMapper requestMapper;
    private final StatsClientImpl statsClient;

    @Override
    @Transactional
    public EventDtoOut createByPrivate(long userId, EventDtoIn inputDto) {

        checkExistsUserById(userId);
        checkExistsCategoryById(inputDto.getCategory());
        checkEventDate(inputDto.getEventDate(), LocalDateTime.now().minusHours(2));

        Event event = eventMapper.eventDtoToEvent(inputDto, userId, EventState.PENDING);
        Event savedEvent = eventDAO.save(event);

        return eventMapper.eventToEventDto(savedEvent);
    }

    @Override
    @Transactional
    public EventDtoOut updateByPrivate(long userId, long eventId, EventUserDtoUpdate eventUserDtoUpdate) {

        checkExistsUserById(userId);
        checkExistsEventById(eventId);
        checkEventDate(eventUserDtoUpdate.getEventDate(), LocalDateTime.now().minusHours(2));

        Event event = eventDAO.findByIdAndInitiator_Id(eventId, userId)
                .orElseThrow(() -> NotFoundException.builder()
                        .message(String.format("The event with the ID=`%d` and initiator ID=`%d` was not found.", eventId, userId))
                        .build());

        if (Objects.equals(EventState.PUBLISHED, event.getState())) {
            throw DataConflictException.builder()
                    .message(String.format("An event with ID=`%d` has been published, it cannot be edited.", eventId))
                    .build();
        }

        StateAction stateAction = eventUserDtoUpdate.getStateAction();
        if (Objects.nonNull(stateAction)) {
            updateEventState(stateAction, event);
        }
        Long categoryId = eventUserDtoUpdate.getCategoryId();
        if (Objects.nonNull(categoryId)) {
            updateEventCategory(categoryId, event);
        }
        Event updatedEvent = eventMapper.update(eventUserDtoUpdate, event);
        eventDAO.save(updatedEvent);

        return eventMapper.eventToEventDto(updatedEvent);
    }

    @Override
    @Transactional
    public EventDtoOut updateByAdmin(long eventId, EventAdminDtoUpdate eventAdminDtoUpdate) {

        Event event = eventDAO.findById(eventId)
                .orElseThrow(() -> NotFoundException.builder()
                        .message(String.format("The event with the ID=`%d` was not found.", eventId))
                        .build());

        String eventDate = eventAdminDtoUpdate.getEventDate();
        LocalDateTime verificationDate = LocalDateTime.now().minusHours(1);
        if (Objects.nonNull(eventDate)) {
            checkEventDate(eventDate, verificationDate);
        } else {
            checkEventDate(event.getEventDate().format(formatter), verificationDate);
        }

        StateAction stateAction = eventAdminDtoUpdate.getStateAction();
        if (Objects.nonNull(stateAction)) {
            updateEventState(stateAction, event);
        }
        Long categoryId = eventAdminDtoUpdate.getCategoryId();
        if (Objects.nonNull(categoryId)) {
            updateEventCategory(categoryId, event);
        }

        Event updatedEvent = eventMapper.update(eventAdminDtoUpdate, event);
        eventDAO.save(updatedEvent);

        return eventMapper.eventToEventDto(updatedEvent);
    }

    @Override
    @Transactional
    public EventRequestStatusUpdateDtoOut updateRequestsByPrivate(long userId, long eventId, EventRequestStatusUpdateDtoIn inputDto) {

        checkExistsUserById(userId);
        checkExistsEventById(eventId);

        Event event = getEvent(userId, eventId);

        List<Request> confirmedRequests = requestDAO.findAllByEvent_IdAndStatus(eventId, RequestStatus.CONFIRMED);
        Integer participantLimit = event.getParticipantLimit();
        if (participantLimit == 0 || !event.isRequestModeration()) {
            List<ParticipationDtoOut> confirmedParticipationDto = requestMapper.entitiesToDtos(confirmedRequests);
            return new EventRequestStatusUpdateDtoOut(confirmedParticipationDto, Collections.emptyList());
        }

        Integer confirmedRequestCount = confirmedRequests.size();
        int countOfAvailableSeats = participantLimit - confirmedRequestCount;

        if (countOfAvailableSeats < 1) {
            throw DataConflictException.builder()
                    .message("The limit of the event participants has been reached.")
                    .build();
        }
        List<Long> requestIds = inputDto.getRequestIds();
        List<Request> updatingRequests = requestDAO.findAllByIdInAndStatus(requestIds, RequestStatus.PENDING);

        if (requestIds.size() != updatingRequests.size()) {
            throw DataConflictException.builder()
                    .message("The status can only be changed for pending applications.")
                    .build();
        }

        List<Request> rejectedRequests = requestDAO.findAllByEvent_IdAndStatus(eventId, RequestStatus.REJECTED);
        for (Request request : updatingRequests) {
            if (Objects.equals(inputDto.getStatus(), RequestStatus.REJECTED)) {
                request.setStatus(RequestStatus.REJECTED);
                rejectedRequests.add(request);
            } else {
                if (countOfAvailableSeats > 0) {
                    request.setStatus(RequestStatus.CONFIRMED);
                    confirmedRequests.add(request);
                    countOfAvailableSeats--;
                } else {
                    request.setStatus(RequestStatus.REJECTED);
                    rejectedRequests.add(request);
                }
            }
        }

        requestDAO.saveAll(updatingRequests);

        List<ParticipationDtoOut> confirmedParticipationDto = requestMapper.entitiesToDtos(confirmedRequests);
        List<ParticipationDtoOut> rejectedParticipationDto = requestMapper.entitiesToDtos(rejectedRequests);

        return new EventRequestStatusUpdateDtoOut(confirmedParticipationDto, rejectedParticipationDto);
    }

    private Event getEvent(long userId, long eventId) {

        return eventDAO.findByIdAndInitiator_Id(eventId, userId)
                .orElseThrow(() -> NotFoundException.builder()
                        .message(String.format("The event with the ID=`%d` and initiator ID=`%d` was not found.", eventId, userId))
                        .build());
    }

    @Override
    public EventDtoOut getByPublic(long eventId, HttpServletRequest request) {

        Event event = eventDAO.findById(eventId).orElseThrow(() -> NotFoundException.builder()
                .message(String.format("The event with the ID=`%d`", eventId))
                .build());

        if (!Objects.equals(EventState.PUBLISHED, event.getState())) {
            throw NotFoundException.builder()
                    .message(String.format("The event with the ID=`%d` was not published.", eventId))
                    .build();
        }

        List<Long> eventIds = List.of(eventId);
        Long confirmedEventRequests = getConfirmedEventRequestsMap(eventIds).getOrDefault(eventId, 0L);
        Long views = getEventViewsMap(eventIds, LocalDateTime.now().minusYears(1), LocalDateTime.now(), true)
                .getOrDefault(eventId, 0L);
        addHit(request);

        return eventMapper.eventToEventDto(event, views, confirmedEventRequests);
    }

    @Override
    public EventDtoOut getByPrivate(long userId, long eventId, HttpServletRequest request) {

        checkExistsUserById(userId);

        Event event = eventDAO.findById(eventId).orElseThrow(() -> NotFoundException.builder()
                .message(String.format("The event with the ID=`%d`", eventId))
                .build());

        List<Long> eventIds = List.of(eventId);
        Long confirmedEventRequests = getConfirmedEventRequestsMap(eventIds).getOrDefault(eventId, 0L);
        Long views = getEventViewsMap(eventIds, LocalDateTime.now().minusYears(1), LocalDateTime.now(), true)
                .getOrDefault(eventId, 0L);

        if (!Objects.equals(userId, event.getInitiator().getId())) {
            addHit(request);
        }

        return eventMapper.eventToEventDto(event, views, confirmedEventRequests);
    }

    @Override
    public List<EventShortDtoOut> getAllByPublic(EventPublicFilter filter, boolean onlyAvailable, String sort, int from, int size, HttpServletRequest request) {

        checkEventFilter(filter);

        Pageable pageable = getPageable(from, size, sort);
        QEvent qEvent = QEvent.event;
        Predicate predicate = QPredicateBuilder.builder()
                .add(filter.getText(), text -> qEvent.description.containsIgnoreCase(text)
                        .or(qEvent.annotation.containsIgnoreCase(text)))
                .add(filter.getPaid(), qEvent.paid::eq)
                .add(filter.getCategories(), qEvent.category.id::in)
                .add(filter.getRangeStart(), qEvent.eventDate::after)
                .add(filter.getRangeEnd(), qEvent.eventDate::before)
                .add(EventState.PUBLISHED, qEvent.state::eq)
                .build();

        List<Event> events = eventDAO.findAll(predicate, pageable).getContent();

        if (events.isEmpty()) {
            return Collections.emptyList();
        }

        List<Long> eventsIds = events.stream().map(Event::getId).collect(Collectors.toList());
        Map<Long, Long> confirmedEventRequestsMap = getConfirmedEventRequestsMap(eventsIds);
        Map<Long, Long> eventViewsMap = getEventViewsMap(eventsIds, LocalDateTime.now().minusYears(1), LocalDateTime.now(), true);

        if (onlyAvailable) {
            events = events.stream()
                    .filter(event -> (event.getParticipantLimit() == 0) ||
                            (event.getParticipantLimit() > confirmedEventRequestsMap.getOrDefault(event.getId(), 0L)))
                    .collect(Collectors.toList());
        }

        List<EventShortDtoOut> response = eventMapper.eventsToEventShortDtos(events);

        response.forEach(event -> {
            Long eventId = event.getId();
            event.setViews(eventViewsMap.getOrDefault(eventId, 0L));
            event.setConfirmedRequests(confirmedEventRequestsMap.getOrDefault(eventId, 0L));
        });

        Comparator<EventShortDtoOut> comparator = getEventShortDtoOutComparator(sort);

        addHit(request);
        return response.stream()
                .sorted(comparator)
                .collect(Collectors.toList());
    }

    @Override
    public List<EventShortDtoOut> getAllByPrivate(long userId, int from, int size) {

        checkExistsUserById(userId);

        Pageable pageable = getPageable(from, size, null);
        Predicate userPredicate = QEvent.event.initiator.id.eq(userId);

        List<Event> events = eventDAO.findAll(userPredicate, pageable).getContent();

        if (events.isEmpty()) {
            return Collections.emptyList();
        }

        List<Long> eventsIds = events.stream().map(Event::getId).collect(Collectors.toList());
        Map<Long, Long> confirmedEventRequestsMap = getConfirmedEventRequestsMap(eventsIds);
        Map<Long, Long> eventViewsMap = getEventViewsMap(eventsIds, LocalDateTime.now().minusYears(1), LocalDateTime.now(), true);
        List<EventShortDtoOut> response = eventMapper.eventsToEventShortDtos(events);

        return response.stream()
                .peek(event -> {
                    Long eventId = event.getId();
                    event.setViews(eventViewsMap.getOrDefault(eventId, 0L));
                    event.setConfirmedRequests(confirmedEventRequestsMap.getOrDefault(eventId, 0L));
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<EventDtoOut> getAllByAdmin(EventAdminFilter filter, int from, int size) {

        checkEventFilter(filter);

        QEvent qEvent = QEvent.event;
        Pageable pageable = getPageable(from, size, null);
        Predicate predicate = QPredicateBuilder.builder()
                .add(filter.getUsers(), qEvent.initiator.id::in)
                .add(filter.getStates(), qEvent.state::in)
                .add(filter.getCategories(), qEvent.category.id::in)
                .add(filter.getRangeStart(), qEvent.eventDate::after)
                .add(filter.getRangeEnd(), qEvent.eventDate::before)
                .build();

        List<EventDtoOut> response = eventMapper.eventsToEventDtos(eventDAO.findAll(predicate, pageable).getContent());

        if (response.isEmpty()) {
            return Collections.emptyList();
        }

        List<Long> eventsIds = response.stream().map(EventDtoOut::getId).collect(Collectors.toList());
        Map<Long, Long> confirmedEventRequestsMap = getConfirmedEventRequestsMap(eventsIds);
        Map<Long, Long> eventViewsMap = getEventViewsMap(eventsIds, LocalDateTime.now().minusYears(1), LocalDateTime.now(), true);

        return response.stream().peek(event -> {
            Long eventId = event.getId();
            event.setViews(eventViewsMap.getOrDefault(eventId, 0L));
            event.setConfirmedRequests(confirmedEventRequestsMap.getOrDefault(eventId, 0L));
        }).collect(Collectors.toList());
    }

    @Override
    public List<ParticipationDtoOut> getAllRequestsByPrivate(long userId, long eventId) {

        checkExistsUserById(userId);
        boolean isEventFound = eventDAO.existsByIdAndInitiator_Id(eventId, userId);
        if (!isEventFound) {
            throw BadRequestException.builder()
                    .message(String.format("The event with ID=`%d` does not belong to the user with ID=`%d`.", eventId, userId))
                    .build();
        }
        List<Request> requests = requestDAO.findAllByEvent_Id(eventId);

        return requestMapper.entitiesToDtos(requests);
    }

    private void addHit(HttpServletRequest request) {

        String ip = request.getRemoteAddr();
        String uri = request.getRequestURI();
        String dateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern(DATE_TIME_PATTERN));
        StatsDtoIn statsDtoIn = StatsDtoIn.builder()
                .app("ewm-main-service")
                .ip(ip)
                .uri(uri)
                .timestamp(dateTime)
                .build();

        statsClient.postStats(statsDtoIn);
    }

    private void updateEventCategory(Long categoryId, Event event) {

        Category category = categoryDAO.findById(categoryId)
                .orElseThrow(() -> NotFoundException.builder()
                        .message(String.format("The category with the ID=`%d` was not found.", categoryId))
                        .build());

        event.setCategory(category);
    }

    private void updateEventState(StateAction stateAction, Event event) {

        switch (stateAction) {
            case SEND_TO_REVIEW:
                event.setState(EventState.PENDING);
                break;
            case PUBLISH_EVENT:
                if (!Objects.equals(event.getState(), EventState.PENDING)) {
                    throw DataConflictException.builder()
                            .message(String.format("Cannot publish the event because it's not in the right state: %s.", event.getState().toString()))
                            .build();
                }
                event.setState(EventState.PUBLISHED);
                event.setPublishedOn(LocalDateTime.now());
                break;
            case CANCEL_REVIEW:
            case REJECT_EVENT:
                if (Objects.equals(event.getState(), EventState.PUBLISHED)) {
                    throw DataConflictException.builder()
                            .message(String.format("Cannot canceled the event because it's not in the right state: %s.", event.getState().toString()))
                            .build();
                }
                event.setState(EventState.CANCELED);
                break;
            default:
                event.setState(event.getState());
        }
    }

    private Pageable getPageable(int from, int size, String sort) {
        Pageable pageable;
        if (Objects.equals(sort, EventSort.EVENT_DATE.toString())) {
            pageable = EwmPageRequest.of(from, size, Sort.by("eventDate"));
        } else {
            pageable = EwmPageRequest.of(from, size);
        }
        return pageable;
    }

    private Map<Long, Long> getEventViewsMap(List<Long> eventsIds, LocalDateTime start, LocalDateTime end, boolean unique) {

        String eventPrefix = "/events/";
        List<String> uris = eventsIds.stream()
                .map(eventId -> eventPrefix + eventId)
                .collect(Collectors.toList());

        String startDate = start != null ? start.format(formatter) : LocalDateTime.now().minusYears(1).format(formatter);
        String endDate = end != null ? end.format(formatter) : LocalDateTime.now().format(formatter);
        List<StatsDtoOut> views = statsClient.getStats(startDate, endDate, uris, unique);

        return views.stream()
                .collect(Collectors.toMap(
                        stats -> Long.parseLong(stats.getUri().replace("/events/", "")),
                        StatsDtoOut::getHits
                ));
    }

    private Map<Long, Long> getConfirmedEventRequestsMap(List<Long> eventsIds) {

        return requestDAO.findAllByEvent_IdInAndStatus(eventsIds, RequestStatus.CONFIRMED).stream()
                .collect(Collectors.groupingBy(request -> request.getEvent().getId(),
                        Collectors.summingLong(request -> 1)));
    }

    private void checkEventFilter(EventFilter filter) {

        if (Objects.isNull(filter.getRangeStart()) && Objects.isNull(filter.getRangeEnd())) {
            filter.setRangeStart(LocalDateTime.now().withNano(0));
        }
        if (Objects.nonNull(filter.getRangeEnd()) && filter.getRangeStart().isAfter(filter.getRangeEnd())) {
            throw BadRequestException.builder()
                    .message("The beginning of the event search interval cannot be later than the end.")
                    .build();
        }
    }

    private void checkExistsEventById(long eventId) {

        boolean isExist = eventDAO.existsById(eventId);
        if (!isExist) {
            throw NotFoundException.builder()
                    .message(String.format("The event with the ID=`%d` was not found.", eventId))
                    .build();
        }
    }

    private void checkExistsUserById(long userId) {

        boolean isExist = userDAO.existsById(userId);
        if (!isExist) {
            throw NotFoundException.builder()
                    .message(String.format("The user with the ID=`%d` was not found.", userId))
                    .build();
        }
    }

    private void checkExistsCategoryById(long catId) {

        boolean isExist = categoryDAO.existsById(catId);
        if (!isExist) {
            throw NotFoundException.builder()
                    .message(String.format("The category with the ID=`%d` was not found.", catId))
                    .build();
        }
    }

    private void checkEventDate(String eventDate, LocalDateTime verificationDate) {

        if (Objects.nonNull(eventDate)) {
            LocalDateTime parsedEventDateTime = LocalDateTime.parse(eventDate, formatter);
            if (parsedEventDateTime.isBefore(verificationDate)) {
                throw BadRequestException.builder()
                        .message(String.format("Field: eventDate. Error: it should indicate a date that has not yet arrived. Value= %S.", eventDate))
                        .build();
            }
        }
    }

    private Comparator<EventShortDtoOut> getEventShortDtoOutComparator(String sort) {

        return Objects.equals(sort, EventSort.EVENT_DATE.toString()) ?
                Comparator.comparing(EventShortDtoOut::getEventDate).thenComparing(EventShortDtoOut::getViews) :
                Comparator.comparing(EventShortDtoOut::getViews).thenComparing(EventShortDtoOut::getEventDate);
    }
}
