package ru.practicum.ewmmain.event.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewmmain.event.dto.EventDtoIn;
import ru.practicum.ewmmain.event.dto.EventDtoOut;
import ru.practicum.ewmmain.event.dto.EventShortDtoOut;
import ru.practicum.ewmmain.event.service.EventService;
import ru.practicum.ewmmain.request.dto.ParticipationDtoOut;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users/{userId}/events")
public class EventPrivateController {

    private final EventService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EventDtoOut createEvent(@PathVariable @Positive final long userId,
                                   @Valid @RequestBody EventDtoIn inputDTO) {
        log.info("START endpoint `method:POST /users/{userId}/events` (create event), event title: {}.", inputDTO.getTitle());

        return service.create(userId, inputDTO);
    }

    @GetMapping
    public List<EventShortDtoOut> getEvents(@PathVariable @Positive final long userId,
                                            @RequestParam(defaultValue = "0") @PositiveOrZero int from,
                                            @RequestParam(defaultValue = "10") @Positive int size) {

        log.info("START endpoint `method:GET /users/{userId}/events` (get events by user id), user id: {}.", userId);

        return service.getAllByUserId(userId, from, size);
    }

    @GetMapping("/{eventId}")
    public EventDtoOut getEvent(@PathVariable @Positive final long userId,
                                @PathVariable @Positive final long eventId) {

        log.info("START endpoint `method:GET /users/{userId}/events/{eventId}` (get event by user and event id), event id: {}.", eventId);

        return service.getByUserAndEventId(userId, eventId);
    }

    @PatchMapping("/{eventId}")
    public EventDtoOut updateEvent(@PathVariable @Positive final long userId,
                                   @PathVariable @Positive final long eventId,
                                   @Valid @RequestBody EventDtoIn inputDTO) {

        log.info("START endpoint `method:PATCH /users/{userId}/events/{eventId}` (update event by user and event id), event id: {}.", eventId);

        return service.updateByUserAndEventId(userId, eventId, inputDTO);
    }

    @GetMapping("/{eventId}/requests")
    public List<ParticipationDtoOut> getRequestsByEvent(@PathVariable @Positive final long userId,
                                                        @PathVariable @Positive final long eventId) {

        log.info("START endpoint `method:GET /users/{userId}/events/{eventId}/requests` (get requests by user and event id), event id: {}.", eventId);

        return service.getRequestsByEvent(userId, eventId);
    }

    @PatchMapping("/{eventId}/requests")
    public List<ParticipationDtoOut> updateRequestsByEvent(@PathVariable @Positive final long userId,
                                   @PathVariable @Positive final long eventId,
                                   @Valid @RequestBody EventDtoIn inputDTO) {

        log.info("START endpoint `method:PATCH /users/{userId}/events/{eventId}` (update requests by user and event id), event id: {}.", eventId);

        return service.updateRequestsByEvent(userId, eventId, inputDTO);
    }
}
