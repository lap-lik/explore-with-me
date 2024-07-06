package ru.practicum.event.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.event.dto.EventAdminDtoUpdate;
import ru.practicum.event.dto.EventAdminFilter;
import ru.practicum.event.dto.EventDtoOut;
import ru.practicum.event.model.EventState;
import ru.practicum.event.service.EventService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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

        log.info("START endpoint `method:GET /admin/events` (get events by the admin).");

        EventAdminFilter filter = EventAdminFilter.builder()
                .users(users)
                .states(Objects.nonNull(states) ? states.stream().map(EventState::valueOf).collect(Collectors.toList()) : null)
                .categories(categories)
                .rangeStart(rangeStart)
                .rangeEnd(rangeEnd)
                .build();

        return service.getAllByAdmin(filter, from, size);
    }

    @PatchMapping("/{eventId}")
    public EventDtoOut updateEvent(@PathVariable @Positive final long eventId,
                                   @Valid @RequestBody EventAdminDtoUpdate eventAdminDtoUpdate) {

        log.info("START endpoint `method:PATCH /admin/events/{eventId}` (update event by the admin), event id: {}.", eventId);

        return service.updateByAdmin(eventId, eventAdminDtoUpdate);
    }
}
