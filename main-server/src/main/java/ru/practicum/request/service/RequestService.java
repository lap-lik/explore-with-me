package ru.practicum.request.service;

import ru.practicum.request.dto.ParticipationDtoOut;

import java.util.List;

public interface RequestService {

    ParticipationDtoOut create(long userId, long eventId);

    List<ParticipationDtoOut> getAll(long userId);

    ParticipationDtoOut canceled(long userId, long requestId);
}
