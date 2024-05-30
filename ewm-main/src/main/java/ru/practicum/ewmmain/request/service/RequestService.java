package ru.practicum.ewmmain.request.service;

import ru.practicum.ewmmain.request.dto.ParticipationDtoOut;

import java.util.List;

public interface RequestService {

    ParticipationDtoOut create(long userId, long eventId);

    List<ParticipationDtoOut> getAll(long userId);

    ParticipationDtoOut update(long userId, long requestId);
}
