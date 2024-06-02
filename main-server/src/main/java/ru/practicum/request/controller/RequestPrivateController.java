package ru.practicum.request.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.request.dto.ParticipationDtoOut;
import ru.practicum.request.service.RequestService;

import javax.validation.constraints.Positive;
import java.util.List;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users/{userId}/requests")
public class RequestPrivateController {

    private final RequestService service;


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ParticipationDtoOut createRequest(@PathVariable @Positive final long userId,
                                             @RequestParam(name = "eventId", required = false) @Positive final Long eventId) {

        log.info("START endpoint `method:POST /users/{userId}/requests` (create request), event id: {}.", eventId);

        return service.create(userId, eventId);
    }

    @GetMapping
    public List<ParticipationDtoOut> getAllRequests(@PathVariable @Positive final long userId) {

        log.info("START endpoint `method:GET /categories` (get all requests by user id), user id: {}.", userId);

        return service.getAll(userId);
    }


    @PatchMapping("/{requestId}/cancel")
    public ParticipationDtoOut updateRequest(@PathVariable @Positive final long userId,
                                             @PathVariable @Positive final long requestId) {

        log.info("START endpoint `method:PATCH /users/{userId}/requests/{requestId}/cancel` (canceled request by id), request id: {}.", requestId);

        return service.update(userId, requestId);
    }
}
