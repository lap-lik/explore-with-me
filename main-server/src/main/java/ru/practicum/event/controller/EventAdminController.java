package ru.practicum.event.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.event.dto.EventAdminDtoUpdate;
import ru.practicum.event.dto.EventDtoOut;
import ru.practicum.event.service.EventService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.List;

import static ru.practicum.constant.Constant.DATE_TIME_PATTERN;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/admin/events")
public class EventAdminController {

    private final EventService service;

    @GetMapping
    public List<EventDtoOut> getEvents(@RequestParam(required = false) List<Long> users,
                                       @RequestParam(required = false) List<String> states,
                                       @RequestParam(required = false) List<Long> categories,
                                       @RequestParam(required = false) @DateTimeFormat(pattern = DATE_TIME_PATTERN) LocalDateTime rangeStart,
                                       @RequestParam(required = false) @DateTimeFormat(pattern = DATE_TIME_PATTERN) LocalDateTime rangeEnd,
                                       @RequestParam(defaultValue = "0") @PositiveOrZero int from,
                                       @RequestParam(defaultValue = "10") @Positive int size) {

        log.info("START endpoint `method:GET /admin/events` (get events by the admin), user id: {}.", users);

        return service.getAllByTheAdmin(users, states, categories, rangeStart, rangeEnd, from, size);
    }

    @PatchMapping("/{eventId}")
    public EventDtoOut updateEvent(@PathVariable @Positive final long eventId,
                                   @Valid @RequestBody EventAdminDtoUpdate eventAdminDtoUpdate) {

        log.info("START endpoint `method:PATCH /admin/events/{eventId}` (update event by the admin), event id: {}.", eventId);

        return service.updateByTheAdmin(eventId, eventAdminDtoUpdate);
    }
}
