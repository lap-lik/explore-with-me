package ru.practicum.event.service;

import ru.practicum.event.dto.EventDtoIn;
import ru.practicum.event.dto.EventDtoOut;
import ru.practicum.event.dto.EventShortDtoOut;
import ru.practicum.request.dto.ParticipationDtoOut;

import java.util.List;

public interface EventService {

    EventDtoOut create(long userId, EventDtoIn inputDto);

    List<EventShortDtoOut> getAllByUserId(long userId, int from, int size);

    EventDtoOut getByUserAndEventId(long userId, long eventId);

    EventDtoOut updateByUserAndEventId(long userId, long eventId, EventDtoIn inputDto);

    List<ParticipationDtoOut> getRequestsByEvent(long userId, long eventId);

    List<ParticipationDtoOut> updateRequestsByEvent(long userId, long eventId, EventDtoIn inputDto);

    List<EventDtoOut> searchByAdmin(long userId, long eventId);

    List<EventDtoOut> search(long userId, long eventId);
}
