package ru.practicum.event.service;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.StatsClientImpl;
import ru.practicum.StatsDtoOut;
import ru.practicum.category.dao.CategoryDAO;
import ru.practicum.category.model.Category;
import ru.practicum.event.dao.EventDAO;
import ru.practicum.event.dao.LocationDAO;
import ru.practicum.event.dto.*;
import ru.practicum.event.mapper.EventMapper;
import ru.practicum.event.model.Event;
import ru.practicum.event.model.EventState;
import ru.practicum.event.model.Location;
import ru.practicum.event.model.StateAction;
import ru.practicum.exception.BadRequestException;
import ru.practicum.exception.DataConflictException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.request.dao.RequestDAO;
import ru.practicum.request.dto.ParticipationDtoOut;
import ru.practicum.request.model.RequestStatus;
import ru.practicum.user.dao.UserDAO;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static ru.practicum.constant.Constant.DATE_TIME_PATTERN;
import static ru.practicum.event.model.EventState.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EventServiceImpl implements EventService {

    public static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_TIME_PATTERN);
    private final EventDAO eventDAO;
    private final RequestDAO requestDAO;
    private final UserDAO userDAO;
    private final CategoryDAO categoryDAO;
    private final LocationDAO locationDAO;
    private final EventMapper eventMapper;
    private final StatsClientImpl statsClient;

    @Override
    @Transactional
    public EventDtoOut create(long userId, EventDtoIn inputDto) {

        checkExistsUserById(userId);
        checkExistsCategoryById(inputDto.getCategory());
        Location location = getLocation(inputDto.getLocation()); //TODO: заготовка для 3 этапа Администрирование первый вариант (подтверждение создания / корректировка локаций, только администратором)

        return eventMapper.eventToEventDto(eventDAO.save(eventMapper.eventDtoToEvent(inputDto, userId, location, PENDING)), null, null);
    }

    @Override
    public List<EventShortDtoOut> getAllByUserId(long userId, int from, int size) {

        checkExistsUserById(userId);
        List<EventShortDtoOut> events = eventMapper.eventsToEventShortDtos(eventDAO.findAllByUserId(userId, from, size));

        if (events.isEmpty()) {
            return events;
        }

        List<Long> eventsIds = events.stream().map(EventShortDtoOut::getId).collect(Collectors.toList());
        Map<Long, Long> confirmedEventRequestsMap = getConfirmedEventRequestsMap(eventsIds);
        Map<Long, Long> eventViewsMap = getEventViewsMap(eventsIds);

        return events.stream()
                .peek(event -> {
                    Long eventId = event.getId();
                    event.setViews(eventViewsMap.getOrDefault(eventId, 0L));
                    event.setConfirmedRequests(confirmedEventRequestsMap.getOrDefault(eventId, 0L));
                })
                .collect(Collectors.toList());
    }

    @Override
    public EventDtoOut getByUserAndEventId(long userId, long eventId) {

        checkExistsUserById(userId);
        checkExistsEventById(eventId);
        EventDtoOut response = eventMapper.eventToEventDto(eventDAO.findByIdAndInitiator_Id(eventId, userId)
                .orElseThrow(() -> NotFoundException.builder()
                        .message(String.format("The event with the ID=`%d` and initiator ID=`%d` was not found.", eventId, userId))
                        .build()), null, null);

        Map<Long, Long> confirmedEventRequestsMap = getConfirmedEventRequestsMap(List.of(eventId));
        Map<Long, Long> eventViewsMap = getEventViewsMap(List.of(eventId));
        response.setConfirmedRequests(confirmedEventRequestsMap.getOrDefault(eventId, 0L));
        response.setViews(eventViewsMap.getOrDefault(eventId, 0L));

        return response;
    }

    @Override
    @Transactional
    public EventDtoOut updateByUserAndEventId(long userId, long eventId, EventUserDtoUpdate eventUserDtoUpdate) {

        checkExistsUserById(userId);
        checkExistsEventById(eventId);

        Event event = eventDAO.findByIdAndInitiator_Id(eventId, userId)
                .orElseThrow(() -> NotFoundException.builder()
                        .message(String.format("The event with the ID=`%d` and initiator ID=`%d` was not found.", eventId, userId))
                        .build());

        if (Objects.equals(EventState.PUBLISHED, event.getState())) {
            throw DataConflictException.builder()
                    .message(String.format("An event with ID=`%d` has been published, it cannot be edited.", eventId))
                    .build();
        }

        Long confirmedEventRequests = getConfirmedEventRequestsMap(List.of(eventId)).getOrDefault(eventId, 0L);
        Long views = getEventViewsMap(List.of(eventId)).getOrDefault(eventId, 0L);

        return eventMapper.eventToEventDto(eventDAO.save(updateEventFields(event, eventUserDtoUpdate)), views, confirmedEventRequests);
    }

    @Override
    public List<ParticipationDtoOut> getRequestsByEvent(long userId, long eventId) {
        return List.of();
    }

    @Override
    public List<ParticipationDtoOut> updateRequestsByEvent(long userId, long eventId, EventDtoIn inputDto) {
        return List.of();
    }

    @Override
    @Transactional
    public EventDtoOut getById(long eventId, HttpServletRequest request) {

        EventDtoOut response = eventMapper.eventToEventDto(eventDAO.findById(eventId)
                .orElseThrow(() -> NotFoundException.builder()
                        .message(String.format("The event with the ID=`%d` was not found.", eventId))
                        .build()), null, null);

        if (!Objects.equals(PUBLISHED, response.getState())) {
            throw BadRequestException.builder()
                    .message(String.format("The event with the ID=`%d` was not published.", eventId))
                    .build();
        }

        Long confirmedEventRequests = getConfirmedEventRequestsMap(List.of(eventId)).getOrDefault(eventId, 0L);
        Long views = getEventViewsMap(List.of(eventId)).getOrDefault(eventId, 0L);
        response.setConfirmedRequests(confirmedEventRequests);
        response.setViews(views);

        addHit(eventId, request);

        return response;
    }

    private void addHit(long eventId, HttpServletRequest request) {

        String ip = request.getRemoteAddr();
        String uri = request.getRequestURI();
        System.err.println(uri);
    }

    @Override
    public List<EventShortDtoOut> search(String text,
                                    List<Long> categories,
                                    Boolean paid,
                                    LocalDateTime rangeStart,
                                    LocalDateTime rangeEnd,
                                    boolean onlyAvailable,
                                    String sort,
                                    int from,
                                    int size) {
        List<EventShortDtoOut> response = eventMapper.eventsToEventShortDtos(eventDAO.search(
                text,
                categories,
                paid,
                rangeStart,
                rangeEnd,
                PUBLISHED.toString(),
                from,
                size));

        return response;
    }

    @Override
    public List<EventDtoOut> getAllByTheAdmin(List<Long> users,
                                              List<String> states,
                                              List<Long> categories,
                                              LocalDateTime rangeStart,
                                              LocalDateTime rangeEnd,
                                              int from,
                                              int size) {
        return List.of();
    }

    @Override
    @Transactional
    public EventDtoOut updateByTheAdmin(long eventId, EventAdminDtoUpdate eventAdminDtoUpdate) {

        Event event = eventDAO.findById(eventId)
                .orElseThrow(() -> NotFoundException.builder()
                        .message(String.format("The event with the ID=`%d` was not found.", eventId))
                        .build());

        Long confirmedEventRequests = getConfirmedEventRequestsMap(List.of(eventId)).getOrDefault(eventId, 0L);
        Long views = getEventViewsMap(List.of(eventId)).getOrDefault(eventId, 0L);

        return eventMapper.eventToEventDto(eventDAO.save(updateEventFields(event, eventAdminDtoUpdate)), views, confirmedEventRequests);
    }


    private Location getLocation(LocationDto locationDto) {

        try {
            return locationDAO.save(eventMapper.locationDtoToLocation(locationDto));
        } catch (DataIntegrityViolationException e) {
            return locationDAO.findByLatitudeAndLongitude(locationDto.getLat(), locationDto.getLon());
        }
    }

    private void checkExistsRequestById(long requestId) {

        boolean isExist = requestDAO.existsById(requestId);
        if (!isExist) {
            throw NotFoundException.builder()
                    .message(String.format("The request with the ID=`%d` was not found.", requestId))
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

    private Event updateEventFields(Event event, EventUserDtoUpdate eventUserDtoUpdate) {

        String annotation = eventUserDtoUpdate.getAnnotation();
        Long categoryId = eventUserDtoUpdate.getCategoryId();
        String description = eventUserDtoUpdate.getDescription();
        String eventDate = eventUserDtoUpdate.getEventDate();
        LocationDto locationDto = eventUserDtoUpdate.getLocation();
        Boolean paid = eventUserDtoUpdate.getPaid();
        Integer participantLimit = eventUserDtoUpdate.getParticipantLimit();
        Boolean requestModeration = eventUserDtoUpdate.getRequestModeration();
        String title = eventUserDtoUpdate.getTitle();
        StateAction stateAction = eventUserDtoUpdate.getStateAction();

        Event.EventBuilder eventBuilder = event.toBuilder();

        if (Objects.nonNull(annotation)) {
            eventBuilder.annotation(annotation).build();
        }
        if (Objects.nonNull(categoryId)) {
            Category category = categoryDAO.findById(categoryId)
                    .orElseThrow(() -> NotFoundException.builder()
                            .message(String.format("The category with the ID=`%d` was not found.", categoryId))
                            .build());

            eventBuilder.category(category).build();
        }
        if (Objects.nonNull(description)) {
            eventBuilder.description(description).build();
        }
        if (Objects.nonNull(eventDate)) {
            eventBuilder.eventDate(LocalDateTime.parse(eventDate, formatter)).build();
        }
        if (Objects.nonNull(locationDto)) {
            Location location = getLocation(locationDto);
            eventBuilder.location(location).build();
        }
        if (Objects.nonNull(paid)) {
            eventBuilder.paid(paid).build();
        }
        if (Objects.nonNull(participantLimit)) {
            eventBuilder.participantLimit(participantLimit).build();
        }
        if (Objects.nonNull(requestModeration)) {
            eventBuilder.requestModeration(requestModeration).build();
        }
        if (Objects.nonNull(title)) {
            eventBuilder.title(title).build();
        }
        if (Objects.nonNull(stateAction)) {
            EventState state = PENDING;
            switch (stateAction) {
                case SEND_TO_REVIEW:
                    break;
                case PUBLISH_EVENT:
                    state = PUBLISHED;
                    break;
                case CANCEL_REVIEW:
                case REJECT_EVENT:
                    state = CANCELED;
                    break;
            }
            eventBuilder.state(state).build();
        }
        return eventBuilder.build();
    }

    private Map<Long, Long> getConfirmedEventRequestsMap(List<Long> eventsIds) {

        return requestDAO.findAllByEvent_IdInAndStatus(eventsIds, RequestStatus.CONFIRMED.toString()).stream()
                .collect(Collectors.groupingBy(request -> request.getEvent().getId(),
                        Collectors.summingLong(request -> 1)));
    }

    private Map<Long, Long> getEventViewsMap(List<Long> eventsIds) {

        String eventPrefix = "/events/";
        List<String> uris = new ArrayList<>();
        for (long eventId : eventsIds) {
            uris.add(eventPrefix + eventId);
        }

        String startDate = LocalDateTime.of(1900, 1, 1, 0, 0).format(formatter);
        String endDate = LocalDateTime.now().format(formatter);
        List<StatsDtoOut> views = statsClient.getStats(startDate, endDate, uris, true);

        return views.stream()
                .collect(Collectors.toMap(
                        stats -> Long.parseLong(stats.getUri().replace("/events/", "")),
                        StatsDtoOut::getHits
                ));
    }
}
