package ru.practicum.event.service;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.RequestParam;
import ru.practicum.event.dto.*;
import ru.practicum.request.dto.ParticipationDtoOut;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.List;

import static ru.practicum.constant.Constant.DATE_TIME_PATTERN;

public interface EventService {

    EventDtoOut create(long userId, EventDtoIn inputDto);

    List<EventShortDtoOut> getAllByUserId(long userId, int from, int size);

    EventDtoOut getByUserAndEventId(long userId, long eventId);

    EventDtoOut updateByUserAndEventId(long userId, long eventId, EventUserDtoUpdate eventUserDtoUpdate);

    List<ParticipationDtoOut> getRequestsByEvent(long userId, long eventId);

    List<ParticipationDtoOut> updateRequestsByEvent(long userId, long eventId, EventDtoIn inputDto);

    EventDtoOut getById(long eventId, HttpServletRequest request);

    List<EventShortDtoOut> search(String text,
                             List<Long> categories,
                             Boolean paid,
                             LocalDateTime rangeStart,
                             LocalDateTime rangeEnd, boolean onlyAvailable, String sort, int from, int size);

    List<EventDtoOut> getAllByTheAdmin(List<Long> users,
                                       List<String> states,
                                       List<Long> categories,
                                       LocalDateTime rangeStart,
                                       LocalDateTime rangeEnd,
                                       int from,
                                       int size);

    EventDtoOut updateByTheAdmin(long eventId, EventAdminDtoUpdate eventAdminDtoUpdate);
}
