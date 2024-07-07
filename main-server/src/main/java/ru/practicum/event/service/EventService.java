package ru.practicum.event.service;

import ru.practicum.event.dto.*;
import ru.practicum.request.dto.ParticipationDtoOut;

import java.util.List;

public interface EventService {

    EventDtoOut createByPrivate(long userId, EventDtoIn inputDto);

    EventDtoOut updateByPrivate(long userId, long eventId, EventUserDtoUpdate eventUserDtoUpdate);

    EventDtoOut updateByAdmin(long eventId, EventAdminDtoUpdate eventAdminDtoUpdate);

    EventRequestStatusUpdateDtoOut updateRequestsByPrivate(long userId, long eventId, EventRequestStatusUpdateDtoIn inputDto);

    EventDtoOut getByPublic(long eventId, String ip, String uri);

    EventDtoOut getByPrivate(long userId, long eventId, String ip, String uri);

    List<EventShortDtoOut> getAllByPublic(EventPublicFilter filter, boolean onlyAvailable, String sort, int from, int size, String ip, String uri);

    List<EventShortDtoOut> getAllByPrivate(long userId, int from, int size);

    List<EventDtoOut> getAllByAdmin(EventAdminFilter filter, int from, int size);

    List<ParticipationDtoOut> getAllRequestsByPrivate(long userId, long eventId);
}
