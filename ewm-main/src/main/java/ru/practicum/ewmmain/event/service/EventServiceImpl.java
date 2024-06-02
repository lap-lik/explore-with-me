package ru.practicum.ewmmain.event.service;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ru.practicum.StatsDtoOut;
import ru.practicum.client.StatsClient;
import ru.practicum.ewmmain.category.dao.CategoryDAO;
import ru.practicum.ewmmain.event.dao.EventDAO;
import ru.practicum.ewmmain.event.dao.LocationDAO;
import ru.practicum.ewmmain.event.dto.EventDtoIn;
import ru.practicum.ewmmain.event.dto.EventDtoOut;
import ru.practicum.ewmmain.event.dto.EventShortDtoOut;
import ru.practicum.ewmmain.event.mapper.EventMapper;
import ru.practicum.ewmmain.event.model.Location;
import ru.practicum.ewmmain.exception.NotFoundException;
import ru.practicum.ewmmain.request.dao.RequestDAO;
import ru.practicum.ewmmain.request.dto.ParticipationDtoOut;
import ru.practicum.ewmmain.request.model.RequestStatus;
import ru.practicum.ewmmain.user.dao.UserDAO;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static ru.practicum.ewmmain.constant.Constant.DATE_TIME_PATTERN;
import static ru.practicum.ewmmain.event.model.EventState.PENDING;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EventServiceImpl implements EventService {

    private final EventDAO eventDAO;
    private final RequestDAO requestDAO;
    private final UserDAO userDAO;
    private final CategoryDAO categoryDAO;
    private final LocationDAO locationDAO;
    private final EventMapper eventMapper;
    private final StatsClient statsClient;

    @Override
    @Transactional
    public EventDtoOut create(long userId, EventDtoIn input) {

        checkExistsUserById(userId);
        checkExistsCategoryById(input.getCategory());

        Location location; //TODO: заготовка для 3 этапа Администрирование первый вариант (создание/корректировка локаций только через одобрение администратора)
        try {
            location = locationDAO.save(eventMapper.locationDtoToLocation(input.getLocation()));
        } catch (DataIntegrityViolationException e) {
            location = locationDAO.findByLatitudeAndLongitude(input.getLocation().getLat(), input.getLocation().getLon());
        }

        return eventMapper.eventToEventDto(eventDAO.save(eventMapper.eventDtoToEvent(input, userId, location, PENDING)), null, null);
    }

    @Override
    public List<EventShortDtoOut> getAllByUserId(long userId, int from, int size) {

        List<EventShortDtoOut> events = eventMapper.eventsToEventShortDtos(eventDAO.findAllByUserId(userId, from, size));

        if (!events.isEmpty()) {
            return getEventsWithViewsAndRequests(events);
        }

        return events;
    }

    private List<EventShortDtoOut> getEventsWithViewsAndRequests(List<EventShortDtoOut> events) {

        String eventPrefix = "/events/";
        List<Long> eventIds = new ArrayList<>();
        List<String> uris = new ArrayList<>();
        for (EventShortDtoOut event : events) {
            Long eventId = event.getId();
            eventIds.add(eventId);
            uris.add(eventPrefix + eventId);
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_TIME_PATTERN);
        String startDate = LocalDateTime.of(1900, 1, 1, 0, 0).format(formatter);
        String endDate = LocalDateTime.now().format(formatter);

        Map<Long, Long> confirmedRequests = requestDAO.findAllByEvent_IdInAndStatus(eventIds, RequestStatus.CONFIRMED.toString()).stream()
                .collect(Collectors.groupingBy(request -> request.getEvent().getId(),
                        Collectors.summingLong(request -> 1)));

        List<StatsDtoOut> views = statsClient.getStats(startDate, endDate, uris, true);

        System.err.println("!!!!!!!!!!!!!!!!!!!!!! - 1");

        Map<Long, Long> viewsMap = views.stream()
                .collect(Collectors.toMap(
                        stats -> Long.parseLong(stats.getUri().replace("/events/", "")),
                        StatsDtoOut::getHits
                ));


        System.err.println("!!!!!!!!!!!!!!!!!!!!!! - 2");


        return events.stream()
                .peek(event -> {
                    Long eventId = event.getId();
                    event.setViews(viewsMap.getOrDefault(eventId, 0L));
                    event.setConfirmedRequests(confirmedRequests.getOrDefault(eventId, 0L));
                })
                .collect(Collectors.toList());
    }

    @Override
    public EventDtoOut getByUserAndEventId(long userId, long eventId) {
        return null;
    }

    @Override
    public EventDtoOut updateByUserAndEventId(long userId, long eventId, EventDtoIn inputDto) {
        return null;
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
    public List<EventDtoOut> searchByAdmin(long userId, long eventId) {
        return List.of();
    }

    @Override
    public List<EventDtoOut> search(long userId, long eventId) {
        return List.of();
    }

    private void checkExistsRequestById(long requestId) {

        boolean isExist = requestDAO.existsById(requestId);
        if (!isExist) {
            throw NotFoundException.builder()
                    .message(String.format("The request with the ID=`%d` was not found.", requestId))
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
}
