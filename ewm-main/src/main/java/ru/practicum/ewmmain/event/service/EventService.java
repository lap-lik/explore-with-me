package ru.practicum.ewmmain.event.service;

import ru.practicum.ewmmain.event.dto.EventDtoIn;
import ru.practicum.ewmmain.event.dto.EventDtoOut;
import ru.practicum.ewmmain.event.dto.EventShortDtoOut;

import java.util.List;

public interface EventService {

    EventDtoOut create(Long userId, EventDtoIn input);

    List<EventShortDtoOut> getAllByUserId(Long userId, int from, int size);

    EventDtoOut getByUserAndEventId(Long userId, Long eventId);

    EventDtoOut updateByUserAndEventId(Long userId, Long eventId);
}
